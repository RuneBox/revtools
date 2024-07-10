package io.runebox.revtools.asm.util.collection

interface InheritanceTree<T> : Iterable<InheritanceTree.MemberSet<T>> {
    interface MemberSet<T> : Iterable<T>

    val size: Int
    val subSets: Int

    fun add(element: T): MemberSet<T>
    operator fun get(element: T): MemberSet<T>?
    fun link(a: MemberSet<T>, b: MemberSet<T>)
}

class MemberInheritanceTree<T> : InheritanceTree<T> {
    private class MemberNode<T>(val value: T) : InheritanceTree.MemberSet<T> {
        val children = mutableListOf<MemberNode<T>>()

        private var _parent = this
        var parent
            get() = _parent
            set(value) {
                _parent = value
                _parent.children.add(this)
            }

        var rank = 0

        fun find(): MemberNode<T> {
            if(parent !== this) {
                this._parent = parent.find()
            }
            return parent
        }

        override fun iterator(): Iterator<T> {
            return MemberNodeIterator(find())
        }

        override fun equals(other: Any?): Boolean {
            if(this === other) return true
            if(other !is MemberNode<*>) return false
            return find() === other.find()
        }

        override fun hashCode(): Int {
            return find().value.hashCode()
        }

        override fun toString(): String {
            return find().value.toString()
        }
    }

    private class MemberNodeIterator<T>(root: MemberNode<T>) : Iterator<T> {
        private val queue = ArrayDeque<MemberNode<T>>()

        init {
            queue.add(root)
        }

        override fun hasNext(): Boolean {
            return queue.isNotEmpty()
        }

        override fun next(): T {
            val node = queue.removeFirstOrNull() ?: throw NoSuchElementException()
            queue.addAll(node.children)
            return node.value
        }
    }

    private val nodes = mutableMapOf<T, MemberNode<T>>()

    override val size get() = nodes.size
    override var subSets: Int = 0
        private set

    override fun add(element: T): InheritanceTree.MemberSet<T> {
        val node = findNode(element)
        if(node != null) return node

        subSets++

        val newMemberNode = MemberNode(element)
        nodes[element] = newMemberNode
        return newMemberNode
    }

    override fun get(element: T): InheritanceTree.MemberSet<T>? {
        return findNode(element)
    }

    override fun link(a: InheritanceTree.MemberSet<T>, b: InheritanceTree.MemberSet<T>) {
        require(a is MemberNode<T>)
        require(b is MemberNode<T>)

        val rootA = a.find()
        val rootB = b.find()

        if(rootA == rootB) {
            return
        }

        when {
            rootA.rank < rootB.rank -> rootA.parent = rootB
            rootA.rank > rootB.rank -> rootB.parent = rootA
            else -> {
                rootB.parent = rootA
                rootA.rank++
            }
        }

        subSets--
    }

    private fun findNode(element: T): MemberNode<T>? {
        val node = nodes[element] ?: return null
        return node.find()
    }

    override fun iterator(): Iterator<InheritanceTree.MemberSet<T>> {
        return nodes.values.iterator()
    }
}