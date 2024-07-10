package io.runebox.revtools.asm.bc

import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TryCatchBlockNode

class BcMethod(cls: BcClass, node: MethodNode) : BcMember<MethodNode>(cls, node) {

    override var access = node.access
    override var name = node.name
    override var desc = node.desc

    var exceptions: MutableList<String> = node.exceptions ?: mutableListOf()
    var tryCatchBlocks: MutableList<TryCatchBlockNode> = node.tryCatchBlocks
    var instructions: InsnList = node.instructions
    var maxStack = node.maxStack
    var maxLocals = node.maxLocals

    init {
    }

    override fun commit() {
        node.access = access
        node.name = name
        node.desc = desc
        node.exceptions = exceptions
        node.tryCatchBlocks = tryCatchBlocks
        node.instructions = instructions
        node.maxStack = maxStack
        node.maxLocals = maxLocals
        node.visibleAnnotations = annotations
    }



    override fun toString(): String {
        return "$cls.$name$desc"
    }
}