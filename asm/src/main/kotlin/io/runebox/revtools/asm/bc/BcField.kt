package io.runebox.revtools.asm.bc

import org.objectweb.asm.tree.FieldNode

class BcField(cls: BcClass, node: FieldNode) : BcMember<FieldNode>(cls, node) {

    override var access: Int = node.access
    override var name: String = node.name
    override var desc: String = node.desc
    var value: Any? = node.value

    override fun commit() {
        node.access = access
        node.name = name
        node.desc = desc
        node.value = value
        node.visibleAnnotations = annotations
    }

    override fun toString(): String {
        return "$cls.$name:$desc"
    }
}