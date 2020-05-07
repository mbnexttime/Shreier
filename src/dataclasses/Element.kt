package dataclasses

data class Element(
    val index: Int,
    val value: Int
) {
    fun affectedByPermutation(permutation: Permutation): Element {
        return Element(permutation[index], value)
    }
}