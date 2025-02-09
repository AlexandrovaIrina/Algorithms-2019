package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>>(
    private val delegate: CheckableSortedSet<T>? = null,
    private val from: T? = null,
    private val to: T? = null
): AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(var value: T, var parent: Node<T>? = null) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    init {
        delegate?.forEach {
            if (from != null && to != null) {
                if (it >= from && it < to)
                    add(it)
            } else if (from != null) {
                if (it >= from)
                    add(it)
            } else if (to != null) {
                if (it < to)
                    add(it)
            }
        }
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element, closest)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    override fun remove(element: T): Boolean {
        val removable = find(element) ?: return false
        val parent = removable.parent

        if (removable.left == null || removable.right == null) {
            val successor = if (removable.left != null) removable.left else removable.right
            when {
                parent == null -> root = successor
                parent.left?.value == element -> parent.left = successor
                else -> parent.right = successor
            }
        }
        //удаляемый элемент имеет двух потомков
        else {
            if (removable.right!!.left == null) {
                when {
                    parent == null -> root = removable.right
                    parent.left?.value == element -> parent.left = removable.right
                    else -> parent.right = removable.right
                }
                removable.right!!.left = removable.left
            } else {
                val replaceable = findNext(removable.value)!!
                val replaceableParent = findParent(replaceable.value)
                removable.value = replaceable.value
                if (replaceableParent?.left == replaceable)
                    replaceableParent.left = replaceable.right
                else
                    replaceableParent?.right = replaceable.left
            }
        }
        size--
        return true
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun findParent(value: T) =
        root?.let { findParent(it, null, value) }

    private fun findParent(current: Node<T>, last: Node<T>?, value: T): Node<T>? {
        val comparison = value.compareTo(current.value)
        return when {
            comparison == 0 -> last
            comparison < 0 -> current.left?.let { findParent(it, current, value) }
            else -> current.right?.let { findParent(it, current, value) }
        }
    }

    private fun findNext(element: T): Node<T>? {
        var current = root
        var next: Node<T>? = null
        while (current?.value != null) {
            if (current.value > element) {
                next = current
                current = current.left
            } else {
                current = current.right
            }
        }
        return next
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {
        private val stack = Stack<Node<T>>()
        private var current: Node<T>? = null

        init {
            addPathToMin(root)
            if (stack.isNotEmpty())
                current = stack.peek()
        }


        private fun addPathToMin(start: Node<T>?) {
            if (start == null) return
            stack.push(start)
            addPathToMin(start.left)
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        override fun hasNext(): Boolean = !stack.isEmpty()

        /**
         * Поиск следующего элемента
         * Средняя
         */
        override fun next(): T {
            if (stack.isEmpty()) throw NoSuchElementException()
            current = stack.pop()
            addPathToMin(current?.right)
            return current!!.value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            remove(current?.value)
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        if (toElement < this.first()) throw IllegalArgumentException()
        return SubBinaryTreeSet(this, fromElement, toElement)
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        if (toElement < first()) throw IllegalArgumentException()
        return SubBinaryTreeSet(this, null, toElement)
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        if (fromElement > last()) throw IllegalArgumentException()
        return SubBinaryTreeSet(this, fromElement, null)
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
