package dataclasses


/*
represent set of elements
can be affected by permutation
 */

data class ElementsSet(
    val elements: MutableList<Element> = ArrayList()
) {
    constructor(elementsNumber: Int) : this(MutableList(elementsNumber) { Element(it, it) })

    val size: Int
        get() = elements.size

    fun sortByIndexes() {
        elements.sortBy { it.index }
    }

    fun affectedByPermutation(permutation: Permutation): ElementsSet {
        val result = ElementsSet()
        elements.forEach { result.elements.add(it.affectedByPermutation(permutation)) }
        result.sortByIndexes()
        return result
    }

}