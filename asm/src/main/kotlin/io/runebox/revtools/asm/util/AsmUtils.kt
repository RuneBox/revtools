package io.runebox.revtools.asm.util

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.ByteArrayInputStream
import java.io.InputStream

fun InputStream.toClassNode(flags: Int = ClassReader.EXPAND_FRAMES): ClassNode {
    val reader = ClassReader(this)
    val node = ClassNode()
    reader.accept(node, flags)
    return node
}

fun ByteArray.toClassNode(flags: Int = ClassReader.EXPAND_FRAMES): ClassNode {
    return ByteArrayInputStream(this).toClassNode(flags)
}

fun ClassNode.toByteArray(flags: Int = ClassWriter.COMPUTE_MAXS): ByteArray {
    val writer = ClassWriter(flags)
    this.accept(writer)
    return writer.toByteArray()
}
