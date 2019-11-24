package lesson3

import java.util.*
import kotlin.Comparator
import kotlin.NoSuchElementException

class SubBinaryTreeSet<T : Comparable<T>>(
    private val initialSet: KtBinaryTree<T>,
    private val fromElement: T? = null,
    private val toElement: T? = null
) : AbstractMutableSet<T>(), SortedSet<T> {

    override val size: Int
        get() {
            val first = initialSet.first()
            return if (toElement != null)
                initialSet.count { it >= fromElement ?: first && it < toElement }
            else
                initialSet.count { it >= fromElement ?: first }
        }


    override fun comparator(): Comparator<in T>? = initialSet.comparator()

    override fun subSet(fromElement: T, toElement: T): SortedSet<T> =
        initialSet.subSet(fromElement, toElement)

    override fun headSet(toElement: T): SortedSet<T> =
        initialSet.headSet(toElement)

    override fun tailSet(fromElement: T): SortedSet<T> =
        initialSet.tailSet(fromElement)

    override fun contains(element: T): Boolean  =
        when {
            toElement == null && fromElement == null -> true
            toElement == null -> element >= fromElement!!
            fromElement == null -> element < toElement
            else -> element < toElement && element >= fromElement
        }

    override fun add(element: T): Boolean =
        when {
            toElement == null && fromElement == null -> true
            toElement == null && element >= fromElement!! -> initialSet.add(element)
            fromElement == null && element < toElement!! -> initialSet.add(element)
            element < toElement!! && element >= fromElement!! -> initialSet.add(element)
            else -> false
        }

    override fun remove(element: T): Boolean =
        when {
            toElement == null && fromElement == null -> false
            toElement == null && element >= fromElement!! -> initialSet.remove(element)
            fromElement == null && element < toElement!! -> initialSet.remove(element)
            element < toElement!! && element >= fromElement!! -> initialSet.remove(element)
            else -> false
        }

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        private var current: T? = null
        private var next: T? = null
            set(value) {
                current = next
                field = value
            }
        private val iterator = initialSet.iterator()

        init {
            if (hasNext())
                next = next()
            if (fromElement != null) {
                while (iterator.hasNext() && current!! < fromElement)
                    next = next()
            }
        }

        override fun hasNext(): Boolean {
            if (next == null) return false
            return next!! < toElement ?: return false
        }

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            next = if (iterator.hasNext()) iterator.next() else null
            return current!!
        }

        override fun remove() {
            iterator.remove()
        }

    }

    override fun first(): T {
        val iterator = iterator()
        if (!iterator.hasNext()) throw NoSuchElementException()
        return iterator.next()
    }

    override fun last(): T {
        val iterator = iterator()
        if (!iterator.hasNext()) throw NoSuchElementException()
        var element = iterator.next()
        while (iterator.hasNext()) {
            element = iterator.next()
        }
        return element
    }
}