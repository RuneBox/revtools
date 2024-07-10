package io.runebox.revtools.asm.bc

import io.runebox.revtools.asm.util.toByteArray
import io.runebox.revtools.asm.util.toClassNode
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

class BcClassPool(val classes: MutableList<BcClass> = LinkedList<BcClass>()) {
    constructor(vararg classes: BcClass) : this(classes.toMutableList())

    fun containsClass(name: String) = classes.any { it.name == name }
    fun getClass(name: String) = classes.firstOrNull { it.name == name }

    fun addClass(cls: BcClass): Boolean {
        if(containsClass(cls.name)) return false
        classes.add(cls)
        return true
    }

    fun removeClass(cls: BcClass): Boolean {
        if(!containsClass(cls.name)) return false
        classes.remove(cls)
        return true
    }

    fun removeClass(name: String): Boolean {
        if(!containsClass(name)) return false
        classes.remove(getClass(name))
        return true
    }

    fun readClass(input: InputStream) = BcClass(this, input.toClassNode())
    fun readClass(bytes: ByteArray) = BcClass(this, bytes.toClassNode())

    fun commit() {
        for(cls in classes) cls.commit()
    }

    fun buildHierarchy() {
        for(cls in classes) cls.buildClassHierarchy()
        for(cls in classes) cls.buildMemberHierarchy()
    }

    fun toJarFile(file: File, skipCommit: Boolean = false) {
        if(file.exists()) file.deleteRecursively()
        if(file.parentFile?.exists() != false) file.parentFile?.mkdirs()
        file.createNewFile()
        if(skipCommit) commit()
        JarOutputStream(file.outputStream()).use { jos ->
            for(cls in classes) {
                jos.putNextEntry(JarEntry("${cls.name}.class"))
                jos.write(cls.node.toByteArray())
                jos.closeEntry()
            }
        }
    }
}