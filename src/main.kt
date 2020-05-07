import dataclasses.*

fun testGroupOrderByS4() {
    val base: MutableList<Element> = ArrayList()
    for (i in 0..2) {
        base.add(Element(i, i))
    }

    val permutations: MutableList<Permutation> = ArrayList()

    permutations.add(Permutation(mutableListOf(1, 0, 2, 3)))
    permutations.add(Permutation(mutableListOf(1, 2, 3, 0)))


    val chain = FullChainOfStabilizers(
        4,
        base,
        permutations
    )

    println("Order of S4 is " + chain.orderOfGroup)
}

fun testGroupOrderByA5() {
    val base: MutableList<Element> = ArrayList()
    for (i in 0..3) {
        base.add(Element(i, i))
    }

    val permutations: MutableList<Permutation> = ArrayList()

    permutations.add(Permutation(mutableListOf(1, 2, 0, 3, 4)))
    permutations.add(Permutation(mutableListOf(1, 2, 3, 4, 0)))


    val chain = FullChainOfStabilizers(
        5,
        base,
        permutations
    )

    println("Order of A5 is " + chain.orderOfGroup)
}

fun testGroupOrderByV4() {
    val base: MutableList<Element> = ArrayList()
    for (i in 0..4) {
        base.add(Element(i, i))
    }

    val permutations: MutableList<Permutation> = ArrayList()

    permutations.add(Permutation(mutableListOf(1, 0, 3, 2, 4)))
    permutations.add(Permutation(mutableListOf(2, 3, 0, 1, 4)))


    val chain = FullChainOfStabilizers(
        5,
        base,
        permutations
    )

    println("Order of V4 is " + chain.orderOfGroup)
}

fun main() {
    testGroupOrderByS4()
    testGroupOrderByA5()
    testGroupOrderByV4()

   solveTask1()
}