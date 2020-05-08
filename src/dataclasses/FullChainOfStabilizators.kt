package dataclasses

data class FullChainOfStabilizers(
    val n: Int,
    val base: MutableList<Element>,
    val forming: MutableList<Permutation>
) {
    val chain: MutableList<Branch> = ArrayList()
    val orderOfGroup: Long
        get() {
            var result: Long = 1
            chain.forEach {
                result *= it.tree.sequences.keys.size
            }
            return result
        }

    // check all tress and get string set
    val strongSet: MutableList<Permutation>
        get() {
            val result: MutableList<Permutation> = ArrayList()
            val alreadyInStrongSet: HashMap<Permutation, Boolean> = HashMap()
            chain.forEach {
                it.tree.sequences.values.forEach {
                    it.forEach {
                        if (!alreadyInStrongSet.containsKey(it)) {
                            alreadyInStrongSet[it] = true
                            result.add(it)
                        }
                    }
                }
            }
            return result
        }

    // check all trees and get all permutations
    val allPermutations: MutableList<Permutation>
        get() {
            val result: MutableList<Permutation> = mutableListOf(Permutation(n))

            for (i in base.indices) {
                val size = result.size
                for (set in chain[i].tree.keys.keys) {
                    val way = chain[i].tree.keys[set]!!
                    if (way.isNeutral()) continue
                    val rev = way.reversed()

                    for (j in 0 until size) {
                        result.add(result[j].multipliedFromLeft(rev))
                    }
                }
            }
            return result
        }

    init {
        // add full group, G_0
        chain.add(
            Branch(
                forming,
                Tree(n, ElementsSet(mutableListOf(base.first())), forming)
            )
        )
        for (i in 1 until base.size) {
            // we take G_i and make G_{i+1}
            val formings = chain.last().tree.getStabilizersOfRoot()
            val restrictedFormings = reduceForming(formings)

            val newBranch = Branch(
                restrictedFormings,
                Tree(n, ElementsSet(mutableListOf(base[i])), restrictedFormings)
            )

            chain.add(newBranch)
        }
    }

    // check if group contains element
    // return null if elements is not in group, return null
    // else return permutation, that has inside realization by formings
    fun checkIfGroupContainsPermutation(permutation: Permutation): Permutation? {
        val realization: MutableList<Permutation> = ArrayList()
        val flag = checkIfPermutationInIthBranch(0, permutation, realization)
        if (flag) return Tree.mergePermutations(n, realization)
        return null
    }

    fun checkIfPermutationInIthBranch(
        i: Int,
        permutation: Permutation,
        realization: MutableList<Permutation>
    ): Boolean {
        if (i == base.size) return permutation.isNeutral()
        // elementToCheck is where permutation send i-th
        val elementToCheck = Element(i, i).affectedByPermutation(permutation)
        val set = ElementsSet(mutableListOf(elementToCheck))
        return if (!chain[i].tree.containsSet(set)) {
            // if it is not in orbit
            false
        } else {
            // check if permutation in G_{i+1}
            val revPermutation = chain[i].tree.keys[set]!!.reversed()
            realization.add(revPermutation.reversed())
            val newPermutation = permutation.multipliedFromLeft(revPermutation)
            checkIfPermutationInIthBranch(i + 1, newPermutation, realization)
        }
    }


    // logic function, helps to reduce amount of generators
    fun processForm(ind: Int, s: Permutation, checker: HashMap<Pair<Int, Int>, Permutation>): Permutation {
        if (ind == n) return s

        val j = s[ind]
        val pair = Pair(ind, j)

        return if (j != ind) {
            if (checker.containsKey(pair)) {
                processForm(ind + 1, checker[pair]!!.multipliedFromLeft(s.reversed()), checker)
            } else {
                checker[pair] = s
                s
            }
        } else {
            processForm(ind + 1, s, checker)
        }
    }

    // reduce amount of generators
    fun reduceForming(permutations: MutableList<Permutation>): MutableList<Permutation> {
        val result: MutableList<Permutation> = ArrayList()
        val checker: HashMap<Pair<Int, Int>, Permutation> = HashMap()

        for (permutation in permutations) {
            val processed = processForm(0, permutation, checker)
            if (!processed.isNeutral()) result.add(processed)
        }

        return result
    }

    // get subgroup that stabilize first i elements of base
    fun getSubgroup(i: Int): FullChainOfStabilizers {
        return FullChainOfStabilizers(n, base.subList(i, base.size), chain[i].formings)
    }


}