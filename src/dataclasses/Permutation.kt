package dataclasses

// realization is how to make our permutation by formings

data class Permutation(
    val permutation: MutableList<Int>,
    val realization: MutableList<Int> = ArrayList()
) {
    constructor(numberOfElements: Int) : this(MutableList(numberOfElements) { it })

    val size: Int
        get() = permutation.size

    // where i-th element goes
    operator fun get(index: Int): Int {
        return permutation[index]
    }

    // reverse permutation
    fun reversed(): Permutation {
        val result = Permutation(size)
        for (i in 0 until size) {
            result.permutation[permutation[i]] = i
        }
        for (permutation in realization.reversed()) {
            result.realization.add(-permutation)
        }
        return result
    }

    // multiply by other from left
    fun multipliedFromLeft(other: Permutation): Permutation {
        val result = MutableList(size) { it }
        for (i in 0 until size) {
            result[i] = other[this[i]]
        }
        // we must keep realization correct
        return Permutation(result, (other.realization + realization).toMutableList())
    }

    fun isNeutral(): Boolean {
        for (i in 0 until size) if (permutation[i] != i) return false
        return true
    }

}