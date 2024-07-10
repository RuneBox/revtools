package io.runebox.revtools.asm.bc

import io.runebox.revtools.asm.BcClassPool
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

data class MemberRef(val owner: String, val name: String, val desc: String) {
    constructor(owner: String, method: BcMethod) : this(owner, method.name, method.desc)
    constructor(owner: String, method: MethodNode) : this(owner, method.name, method.desc)
    constructor(method: BcMethod) : this(method.cls.name, method)
    constructor(owner: String, field: BcField) : this(owner, field.name, field.desc)
    constructor(owner: String, field: FieldNode) : this(owner, field.name, field.desc)
    constructor(field: BcField) : this(field.cls.name, field)
    constructor(insn: MethodInsnNode) : this(insn.owner, insn.name, insn.desc)
    constructor(insn: FieldInsnNode) : this(insn.owner, insn.name, insn.desc)
    constructor(owner: String, def: MemberDef) : this(owner, def.name, def.desc)

    fun toDef() = MemberDef(name, desc)
    fun newOwner(newOwner: String) = MemberRef(newOwner, name, desc)

    val isMethod get() = desc.startsWith("(")
    val isField get() = !isMethod

    fun resolveMethod(pool: BcClassPool): BcMethod? {
        val cls = pool.getClass(owner) ?: return null
        return cls.lookupMethod(name, desc)
    }

    fun resolveField(pool: BcClassPool): BcField? {
        val cls = pool.getClass(owner) ?: return null
        return cls.lookupfield(name, desc)
    }

    override fun toString(): String {
        return "$owner $name $desc"
    }
}