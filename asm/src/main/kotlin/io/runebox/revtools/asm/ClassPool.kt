package io.runebox.revtools.asm

import io.runebox.revtools.asm.bc.BcClass
import io.runebox.revtools.asm.util.toByteArray
import io.runebox.revtools.asm.util.toClassNode
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

interface ClassPool<T> {
    val classes: MutableList<T>

    fun containsClass(name: String): Boolean

    fun findClass(name: String): T?
    fun addClass(cls: T): Boolean
    fun removeClass(cls: T): Boolean
    fun removeClass(name: String): Boolean

    fun readClass(input: InputStream): T
    fun readClass(bytes: ByteArray): T
}