package io.runebox.revtools.asm.bc

import io.runebox.revtools.asm.BcClassPool
import org.objectweb.asm.tree.AnnotationNode

sealed class BcNode<T>(val pool: BcClassPool, val node: T) {
    abstract var access: Int
    abstract var name: String

    open var annotations: MutableList<AnnotationNode> = mutableListOf()

    abstract fun commit()

    override fun toString(): String {
        return name
    }
}