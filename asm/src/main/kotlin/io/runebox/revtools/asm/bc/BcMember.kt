package io.runebox.revtools.asm.bc

sealed class BcMember<T>(var cls: BcClass, node: T) : BcNode<T>(cls.pool, node) {

    abstract override var access: Int
    abstract override var name: String
    abstract var desc: String

    lateinit var hierarchy: MutableSet<BcMember<T>>
    internal val isHierarchyUnset: Boolean get() {
        return !::hierarchy.isInitialized
    }

    override fun toString(): String {
        return "$cls.$name:$desc"
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is BcMember<*>) return false
        if(cls != other.cls) return false
        if(name != other.name) return false
        if(desc != other.desc) return false
        return true
    }

    override fun hashCode(): Int {
        var result = cls.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + desc.hashCode()
        return result
    }
}