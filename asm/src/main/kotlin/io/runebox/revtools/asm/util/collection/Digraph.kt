package io.runebox.revtools.asm.util.collection

import java.util.*

/**
 * @author Tyler Sedlar
 */
open class Digraph<V, E> : Iterable<V> {

    private val graph = mutableMapOf<V, MutableSet<E>>()

    fun edgeAt(index: Int): MutableSet<E> {
        return graph.values.toTypedArray()[index]
    }

    fun size(): Int {
        return graph.size
    }

    fun containsVertex(vertex: V): Boolean {
        return graph.containsKey(vertex)
    }

    fun containsEdge(vertex: V, edge: E): Boolean {
        return graph.containsKey(vertex) && graph[vertex]!!.contains(edge)
    }

    fun addVertex(vertex: V): Boolean {
        if (graph.containsKey(vertex)) {
            return false
        }
        graph[vertex] = HashSet()
        return true
    }

    fun addEdge(start: V, dest: E) {
        if (!graph.containsKey(start)) {
            return
        }
        graph[start]!!.add(dest)
    }

    fun removeEdge(start: V, dest: E) {
        if (!graph.containsKey(start)) {
            return
        }
        graph[start]!!.remove(dest)
    }

    fun edgesFrom(node: V): Set<E> {
        return Collections.unmodifiableSet(graph[node])
    }

    fun consume(graph: Digraph<V, E>) {
        this.graph.putAll(graph.graph)
    }

    override fun iterator(): Iterator<V> {
        return graph.keys.iterator()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (v in graph.keys) {
            builder.append("\n    ").append(v).append(" -> ").append(graph[v])
        }
        return builder.toString()
    }

    fun flush() {
        graph.clear()
    }
}