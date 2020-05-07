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

    // we want to reduce amount of forming
    fun reduceForming(permutations: MutableList<Permutation>): MutableList<Permutation> {
        val result: MutableList<Permutation> = ArrayList()
        val checker: HashMap<Pair<Int, Int>, Permutation> = HashMap()

        for (permutation in permutations) {
            val processed = processForm(0, permutation, checker)
            if (!processed.isNeutral()) result.add(processed)
        }

        return result
    }

}