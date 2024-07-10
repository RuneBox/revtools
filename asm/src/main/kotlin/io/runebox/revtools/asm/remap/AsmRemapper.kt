package io.runebox.revtools.asm.remap

import io.runebox.revtools.asm.bc.BcClassPool
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.tree.AbstractInsnNode

open class AsmRemapper(
    private val pool: BcClassPool,
    private val mappings: MappingSet
) : Remapper() {

    override fun map(internalName: String): String {
        return mappings.mapClassName(internalName, internalName)
    }

    open fun getFieldInitializer(owner: String, name: String, desc: String): List<AbstractInsnNode>? {
        return null
    }

    open fun mapMethodOwner(owner: String, name: String, desc: String): String {
        return mapType(owner)
    }

    open fun mapFieldOwner(owner: String, name: String, desc: String): String {
        return mapType(owner)
    }
}