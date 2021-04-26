package basics.treeoperations

import kotlin.math.max

sealed class Tree<T> {
    override fun toString(): String = when (this) {
        is Leaf -> value.toString()
        is Node -> "($left, $right)"
    }

    abstract fun count(): Int
    abstract fun countAll(): Int
    abstract fun height(): Int
    abstract fun biggest(): T
    abstract fun sum(): Int
    abstract fun contains(t: T): Boolean

}

data class Leaf<T : Comparable<T>>(val value: T) : Tree<T>() {
    override fun count() = 1
    override fun countAll() = count()
    override fun height() = 1
    override fun biggest() = value
    override fun sum(): Int = value.toString().toInt()
    override fun contains(t: T) = t == value
    operator fun plus(leaf: Leaf<T>) = Node(this, leaf)
}

data class Node<T : Comparable<T>>(val left: Tree<T>, val right: Tree<T>) : Tree<T>() {
    override fun count() = left.count() + right.count()
    override fun countAll() = 1 + left.countAll() + right.countAll()
    override fun height() = 1 + max(left.height(), right.height())
    override fun biggest() = maxOf(left.biggest(), right.biggest())
    override fun sum() = left.sum() + right.sum()
    override operator fun contains(t: T) = left.contains(t) || right.contains(t)
    operator fun plus(node: Node<T>) = Node(this, node)
}
