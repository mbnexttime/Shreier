package dataclasses

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Tree(
    val n: Int,
    val rootSet: ElementsSet,
    val permutations: MutableList<Permutation> = ArrayList()
) {
    /*
    for every elementset x, contains list of permutations, that must be
    applyed to rootset in order from first to last to get x
   */
    val sequences: HashMap<ElementsSet, MutableList<Permutation>> = HashMap()
    val keys: HashMap<ElementsSet, Permutation> = HashMap()

    init {
        rootSet.sortByIndexes()
        sequences[rootSet] = ArrayList()


        val currentSets: Queue<ElementsSet> = LinkedList(listOf(rootSet))


        // use standard bfs to build tree
        while (!currentSets.isEmpty()) {
            val currentSet = currentSets.remove()

            permutations.forEach { permutation: Permutation ->
                val resultingSet = currentSet.affectedByPermutation(permutation)
                if (!sequences.containsKey(resultingSet)) {
                    // copy path to currentset and add last permutation
                    sequences[resultingSet] = ArrayList()
                    sequences[currentSet]!!.forEach { sequences[resultingSet]!!.add(it) }
                    sequences[resultingSet]!!.add(permutation)
                    currentSets.add(resultingSet)
                }
            }
        }

        sequences.keys.forEach {
            keys[it] = mergePermutations(sequences[it]!!)
        }
    }

    // use Shreier lemma and get forming for stabilizer of root
    fun getStabilizersOfRoot(): MutableList<Permutation> {
        val result: MutableList<Permutation> = ArrayList()
        for (set in keys.keys) {
            for (permutation in permutations) {
                val h_y = keys[set]!!
                val reversed_h_sy = keys[set.affectedByPermutation(permutation)]!!.reversed()
                result.add(h_y.multipliedFromLeft(permutation).multipliedFromLeft(reversed_h_sy))
            }
        }
        return result
    }

    fun mergePermutations(permutations: MutableList<Permutation>): Permutation {
        var result = Permutation(n)
        permutations.forEach { result = result.multipliedFromLeft(it) }
        return result
    }


    fun containsSet(set: ElementsSet): Boolean {
        return sequences.containsKey(set)
    }
}