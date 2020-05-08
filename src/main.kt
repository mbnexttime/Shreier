import dataclasses.Element
import dataclasses.FullChainOfStabilizers
import dataclasses.Permutation
import kotlin.test.assertTrue

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

fun testGroupOrderByStrangeSubGroupInS6() {
    val base: MutableList<Element> = ArrayList()
    for (i in 0..4) {
        base.add(Element(i, i))
    }

    val permutations: MutableList<Permutation> = ArrayList()

    permutations.add(Permutation(mutableListOf(1, 4, 2, 3, 5, 0)))
    permutations.add(Permutation(mutableListOf(1, 2, 3, 0, 4, 5)))


    val chain = FullChainOfStabilizers(
        6,
        base,
        permutations
    )
    println("Order of strange subgroup in S6 is " + chain.orderOfGroup)
}

fun testCheckInA5() {
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

    assertTrue { chain.checkIfGroupContainsPermutation(Permutation(mutableListOf(1, 0, 2, 3, 4))) == null }
    assertTrue { chain.checkIfGroupContainsPermutation(Permutation(mutableListOf(1, 2, 0, 3, 4))) != null }
    assertTrue { chain.checkIfGroupContainsPermutation(Permutation(mutableListOf(0, 1, 2, 3, 4))) != null }
}

fun testFromSaveliy() {
    val base: MutableList<Element> = mutableListOf(
        Element(0, 3),
        Element(1, 1),
        Element(2, 5),
        Element(3, 0),
        Element(4, 4),
        Element(5, 2)
    )

    val permutations: MutableList<Permutation> = ArrayList()

    permutations.add(Permutation(mutableListOf(5, 0, 1, 2, 3, 4)))
    permutations.add(Permutation(mutableListOf(2, 3, 0, 1, 4, 5)))


    val chain = FullChainOfStabilizers(
        6,
        base,
        permutations
    )
    println("Order of Saveliy test " + chain.orderOfGroup)
}

fun main() {
    testGroupOrderByS4()
    testGroupOrderByA5()
    testGroupOrderByV4()
    testGroupOrderByStrangeSubGroupInS6()
    testCheckInA5()
    testFromSaveliy()

    solveTask1()
    solveTask2()
}