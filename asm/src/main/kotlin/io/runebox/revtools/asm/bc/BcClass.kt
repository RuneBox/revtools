package io.runebox.revtools.asm.bc

import io.runebox.revtools.asm.BcClassPool
import org.objectweb.asm.Opcodes.ACC_PRIVATE
import org.objectweb.asm.Opcodes.ACC_STATIC
import org.objectweb.asm.tree.ClassNode
import java.util.ArrayDeque

class BcClass(pool: BcClassPool, node: ClassNode) : BcNode<ClassNode>(pool, node) {

    override var access: Int = node.access
    override var name: String = node.name
    var superName: String? = node.superName
    var interfaceNames: MutableList<String> = node.interfaces

    var superClass: BcClass? = null
    var interfaceClasses: MutableList<BcClass> = mutableListOf()
    var childClasses: MutableList<BcClass> = mutableListOf()
    
    var methods: MutableList<BcMethod> = node.methods.map { BcMethod(this, it) }.toMutableList()
    var fields: MutableList<BcField> = node.fields.map { BcField(this, it) }.toMutableList()

    fun getMethod(name: String, desc: String) = methods.firstOrNull { it.name == name && it.desc == desc }
    fun getField(name: String, desc: String) = fields.firstOrNull { it.name == name && it.desc == desc }
    
    fun lookupMethod(name: String, desc: String): BcMethod? {
        var ret = getMethod(name, desc)
        if(ret != null) return ret
        if(superClass != null) {
            ret = superClass!!.lookupMethod(name, desc)
            if(ret != null) return ret
        }
        return null
    }

    fun lookupfield(name: String, desc: String): BcField? {
        var ret = getField(name, desc)
        if(ret != null) return ret
        if(superClass != null) {
            ret = superClass!!.lookupfield(name, desc)
            if(ret != null) return ret
        }
        return null
    }
    
    init {
        node.visibleAnnotations = node.visibleAnnotations?.takeIf { it.isNotEmpty() } ?: mutableListOf()
        node.invisibleAnnotations = node.invisibleAnnotations?.takeIf { it.isNotEmpty() } ?: mutableListOf()
        annotations.addAll(node.visibleAnnotations.plus(node.invisibleAnnotations))
    }

    override fun commit() {
        node.access = access
        node.name = name
        node.superName = superName
        node.interfaces = interfaceNames
        node.visibleAnnotations = annotations
        methods.forEach(BcMethod::commit)
        fields.forEach(BcField::commit)
    }

    internal fun buildClassHierarchy() {
        val superCls = superName?.let { pool.getClass(it) }
        if(superCls != null && superClass == null) {
            superClass = superCls
            superCls.childClasses.add(this)
        }
        for(itfCls in interfaceNames.mapNotNull { pool.getClass(it) }) {
            interfaceClasses.add(itfCls)
            itfCls.childClasses.add(this)
        }
    }
    
    @Suppress("DuplicatedCode")
    internal fun buildMemberHierarchy() {
        if(childClasses.isNotEmpty()) return
        
        val methods = hashMapOf<String, BcMethod>()
        val fields = hashMapOf<String, BcField>()
        val queue = ArrayDeque<BcClass>()
        queue.add(this)
        
        var cls: BcClass = this
        while(queue.poll()?.also { cls = it } != null) {
            for(method in cls.methods) {
                var prev: BcMethod = method
                if(method.isHierarchyBarrier()) {
                    if(method.isHierarchyUnset) {
                        method.hierarchy = hashSetOf(method)
                    }
                } else if(methods["${method.name}${method.desc}"]?.also { prev = it } != null) {
                    if(method.isHierarchyUnset) {
                        method.hierarchy = prev.hierarchy
                        method.hierarchy.add(method)
                    } else if(method.hierarchy != prev.hierarchy) {
                        for(m in prev.hierarchy) {
                            method.hierarchy.add(m)
                            m.hierarchy = method.hierarchy
                        }
                    }
                } else {
                    methods["${method.name}${method.desc}"] = method
                    if(method.isHierarchyUnset) {
                        method.hierarchy = hashSetOf()
                        method.hierarchy.add(method)
                    }
                }
            }

            for(field in cls.fields) {
                var prev: BcField = field
                if(field.isHierarchyBarrier()) {
                    if(field.isHierarchyUnset) {
                        field.hierarchy = hashSetOf(field)
                    }
                } else if(fields["${field.name}${field.desc}"]?.also { prev = it } != null) {
                    if(field.isHierarchyUnset) {
                        field.hierarchy = prev.hierarchy
                        field.hierarchy.add(field)
                    } else if(field.hierarchy != prev.hierarchy) {
                        for(m in prev.hierarchy) {
                            field.hierarchy.add(m)
                            m.hierarchy = field.hierarchy
                        }
                    }
                } else {
                    fields["${field.name}${field.desc}"] = field
                    if(field.isHierarchyUnset) {
                        field.hierarchy = hashSetOf()
                        field.hierarchy.add(field)
                    }
                }
            }
            
            if(cls.superClass != null) queue.add(cls.superClass!!)
            queue.addAll(cls.interfaceClasses)
        }
    }
    
    private fun BcMember<*>.isHierarchyBarrier(): Boolean {
        return (access and (ACC_PRIVATE or ACC_STATIC)) != 0
    }

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is BcClass) return false
        if(name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}