import dataclasses.Element
import dataclasses.FullChainOfStabilizers
import dataclasses.Permutation

// + = по часовой стрелке, - = против часовой


/*
Представим кубик рубика как 26 кубиков, которые перемещаются движениями робота. Поэтому работаем в S_26
Пронумеруемся так: робот видит такую часть кубика:
6 7 8
3 4 5
0 1 2
После этого слоя идет такой слой:
13 14 15
11 .. 12
9 10 11
И третий слой, которой находится с невидимой для робота стороны:
23 24 25
20 21 22
17 18 19
 */

// по циклу будем получать всю перестановку
fun getPermutationByCycle(ind: MutableList<Int>): Permutation {
    val result = Permutation(26)
    for (i in 0 until 26) {
        val j = ind.lastIndexOf(i)
        if (j != -1) {
            result.permutation[i] = ind[(j + 2) % 8]
        }
    }
    return result
}

/*
Получим перестановки, которые отвечают за поворот грани по часовой стрелке и против нее
Сначала для передней:
 */
fun getFront(): Permutation {
    val p = getPermutationByCycle(mutableListOf(0, 3, 6, 7, 8, 5, 2, 1))
    p.realization.add(1)
    return p
}

fun getFrontRev(): Permutation {
    val p = getFront()
    return p.reversed()
}

// для левой грани
fun getLeft(): Permutation {
    val p = getPermutationByCycle(mutableListOf(6, 3, 0, 9, 17, 20, 23, 13))
    p.realization.add(2)
    return p
}

fun getLeftRev(): Permutation {
    val p = getLeft()
    return p.reversed()
}


// для правой

fun getRight(): Permutation {
    val p = getPermutationByCycle(mutableListOf(2, 5, 8, 15, 25, 22, 19, 11))
    p.realization.add(3)
    return p
}

fun getRightRev(): Permutation {
    val p = getRight()
    return p.reversed()
}

// для верхней


fun getUp(): Permutation {
    val p = getPermutationByCycle(mutableListOf(8, 7, 6, 13, 23, 24, 25, 15))
    p.realization.add(4)
    return p
}

fun getUpRev(): Permutation {
    val p = getUp()
    return p.reversed()
}


// для нижней


fun getDown(): Permutation {
    val p = getPermutationByCycle(mutableListOf(0, 1, 2, 11, 19, 18, 17, 9))
    p.realization.add(5)
    return p
}

fun getDownRev(): Permutation {
    val p = getDown()
    return p.reversed()
}

// и задней


fun getBack(): Permutation {
    val p = getPermutationByCycle(mutableListOf(19, 22, 25, 24, 23, 20, 17, 18))
    p.realization.add(6)
    return p
}

fun getBackRev(): Permutation {
    val p = getBack()
    return p.reversed()
}

fun getPermutations(): MutableList<Permutation> {
    val permutations: MutableList<Permutation> = ArrayList()

    permutations.add(getBack())
    permutations.add(getBackRev())
    permutations.add(getUp())
    permutations.add(getUpRev())
    permutations.add(getDown())
    permutations.add(getDownRev())
    permutations.add(getFront())
    permutations.add(getFrontRev())
    permutations.add(getLeft())
    permutations.add(getLeftRev())
    permutations.add(getRight())
    permutations.add(getRightRev())

    return permutations
}

fun solveTask1() {
    /*
    Здесь решаем задачу о том, сколько различных кубиков может получиться у робота
    У нас понятно какие действия
    В качестве базы берем [0..26) и запускаем алгоритм Шрайера-Симса
     */

    val base: MutableList<Element> = ArrayList()
    for (i in 0..25) {
        base.add(Element(i, i))
    }

    val permutations: MutableList<Permutation> = getPermutations()

    for (p in permutations) p.realization.clear()


    val chain = FullChainOfStabilizers(
        26,
        base,
        permutations
    )

    println("Ans for the first part of the task is " + chain.orderOfGroup)
}

// Эта функция по перестановке будет давать такую базу, что первыми идут те элементы, которые подвижны в перестановке
fun getBase(permutation: Permutation): MutableList<Element> {
    val result: MutableList<Element> = MutableList(permutation.size) { Element(it, it) }
    result.sortBy { it.affectedByPermutation(permutation).index == it.index }
    return result
}

fun solveTask2() {
    /*
    Здесь решаем задачу о том, чтобы выразить через основные преобразования все действия, которые оставляют на месте одну грань кубика
    Для этого переберем грань, и будем добавлять в массив действия, которые оставляют на месте текущую грань.
    Потом в массив оставим только те элементы, которые оказались уникальными.
    Это и будет ответ.
     */

    /*
    Для начала, делаем такие базы, что первые 8 элементов - та грань, которая должна быть без движения
     */
    val frontBase = getBase(getFront())
    val backBase = getBase(getBack())
    val upBase = getBase(getUp())
    val downBase = getBase(getDown())
    val leftBase = getBase(getLeft())
    val rightBase = getBase(getRight())


    val permutations = getPermutations()
    permutations.forEach { it.realization.clear() }

    val chains: MutableList<FullChainOfStabilizers> = ArrayList()

    for (face in listOf(frontBase, upBase, downBase, leftBase, rightBase, backBase)) {
        val chain = FullChainOfStabilizers(26, face, permutations)
        // получаем подгруппу, которая стабилизирует первые 8 элементов базы
        val stabilizedChain = chain.getSubgroup(8)
        chains.add(stabilizedChain)
    }

    println("Moves that save front face is about " + chains.first().orderOfGroup)

    /*
    Теперь, если мы хотим проверить, что наш элемент стабилизирует какую-то из граней, спрашиваем
    подряд у элементов массива chains. Каждая из этих групп соответствует действиям, которые оставляют на месте соответствующую грань
    Если элемент стабилизирует грань, нам будет так же доступно его разложение в простейшие движения робота
    ( на самом деле недоступно, потому что оно ну очень большое ( только на первой итерации алгоритма необходимо уже 560 элементов) )
     */


}




