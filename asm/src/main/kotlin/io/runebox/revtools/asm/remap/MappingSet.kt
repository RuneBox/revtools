package io.runebox.revtools.asm.remap

import io.runebox.revtools.asm.bc.MemberRef
import io.runebox.revtools.asm.util.collection.InheritanceTree
import java.util.*

data class MappingSet(
    val classes: SortedMap<String, String>,
    val methods: SortedMap<MemberRef, MethodMapping>,
    val fields: SortedMap<MemberRef, FieldMapping>,
) {
    constructor() : this(TreeMap(), TreeMap(), TreeMap())

    fun add(other: MappingSet) {
        classes.putAll(other.classes)
        methods.putAll(other.methods)
        fields.putAll(other.fields)
    }

    fun mapClassName(name: String, default: String) = classes.getOrDefault(name, default)
    
    fun mapMethodName(inheritors: InheritanceTree.MemberSet<MemberRef>, default: String): String {
        for(member in inheritors) {
            val method = methods[member]
            if(method != null) return method.name
        }
        return default
    }
    
    fun mapMethodOwner(inheritors: InheritanceTree.MemberSet<MemberRef>, default: String): String {
        for(member in inheritors) {
            val method = methods[member]
            if(method != null) return method.owner
        }
        return default
    }

    fun mapFieldName(inheritors: InheritanceTree.MemberSet<MemberRef>, default: String): String {
        for(member in inheritors) {
            val field = fields[member]
            if(field != null) return field.name
        }
        return default
    }

    fun mapFieldOwner(inheritors: InheritanceTree.MemberSet<MemberRef>, default: String): String {
        for(member in inheritors) {
            val field = fields[member]
            if(field != null) return field.owner
        }
        return default
    }

    data class MethodMapping(
        val owner: String,
        val name: String,
        val desc: String
    )

    data class FieldMapping(
        val owner: String,
        val name: String
    )
}