package io.runebox.revtools.asm.bc

import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

data class MemberDef(val name: String, val desc: String) {
    constructor(method: BcMethod) : this(method.name, method.desc)
    constructor(method: MethodNode) : this(method.name, method.desc)
    constructor(field: BcField) : this(field.name, field.desc)
    constructor(field: FieldNode) : this(field.name, field.desc)
    constructor(insn: MethodInsnNode) : this(insn.name, insn.desc)
    constructor(insn: FieldInsnNode) : this(insn.name, insn.desc)
    constructor(ref: MemberRef) : this(ref.name, ref.desc)

    fun toRef(owner: String) = MemberRef(owner, this)

    val isMethod get() = desc.startsWith("(")
    val isField get() = !isMethod

    override fun toString(): String {
        return "$name $desc"
    }
}