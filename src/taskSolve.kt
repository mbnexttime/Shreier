import dataclasses.Element
import dataclasses.FullChainOfStabilizers
import dataclasses.Permutation


/*
Нам нужно поддерживать выражение любой перестановки через изначальные образующие,
поэтому образующие и обратные к ним нужно хардкодить
Синглтон реалайзер содержит в себе поле rev, которое по перестановке из образующих выдает обратную к ней и наоборот
 */
object Realizer {
    val rev: HashMap<String, String> = HashMap()
}

// + = по часовой стрелке, - = против часовой

fun initRealizer() {
    for (type in mutableListOf("Правую", "Левую", "Переднюю", "Заднюю", "Нижнюю", "Верхнюю")) {
        Realizer.rev["( $type +)"] = "( $type -)"
        Realizer.rev["( $type -)"] = "( $type +)"
    }
}

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
            result.permutation[i] = ind[(j + 1) % 8]
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
    p.realization.add("( Переднюю +)")
    return p
}

fun getFronRev(): Permutation {
    val p = getFront()
    return p.reversed()
}

// для левой грани
fun getLeft(): Permutation {
    val p = getPermutationByCycle(mutableListOf(6, 3, 0, 9, 17, 20, 23, 13))
    p.realization.add("( Левую +)")
    return p
}

fun getLeftRev(): Permutation {
    val p = getLeft()
    return p.reversed()
}
// для правой

fun getRight(): Permutation {
    val p = getPermutationByCycle(mutableListOf(2, 5, 8, 15, 25, 22, 19, 11))
    p.realization.add("( Правую +)")
    return p
}

fun getRightRev(): Permutation {
    val p = getLeft()
    return p.reversed()
}

// для верхней

fun getUp(): Permutation {
    val p = getPermutationByCycle(mutableListOf(8, 7, 6, 13, 23, 24, 25, 15))
    p.realization.add("( Верхнюю +)")
    return p
}

fun getUpRev(): Permutation {
    val p = getLeft()
    return p.reversed()
}


// для нижней

fun getDown(): Permutation {
    val p = getPermutationByCycle(mutableListOf(0, 1, 2, 11, 19, 18, 17, 9))
    p.realization.add("( Нижнюю +)")
    return p
}

fun getDownRev(): Permutation {
    val p = getLeft()
    return p.reversed()
}

// и задней

fun getBack(): Permutation {
    val p = getPermutationByCycle(mutableListOf(19, 22, 25, 24, 23, 20, 17, 18))
    p.realization.add("( Заднюю +)")
    return p
}

fun getBackRev(): Permutation {
    val p = getLeft()
    return p.reversed()
}

fun solveTask1() {
    initRealizer()
    /*
    Здесь решаем задачу о том, сколько различных кубиков может получиться у робота
    У нас понятно какие действия
    В качестве базы берем [0..26) и запускаем алгоритм Шрайера-Симса
     */

    val base: MutableList<Element> = ArrayList()
    for (i in 0..25) {
        base.add(Element(i, i))
    }

    val permutations: MutableList<Permutation> = ArrayList()

    // Добавляем образующие нашей группы
    permutations.add(getBack())
    permutations.add(getBackRev())
    permutations.add(getUp())
    permutations.add(getUpRev())
    permutations.add(getDown())
    permutations.add(getDownRev())
    permutations.add(getFront())
    permutations.add(getFronRev())
    permutations.add(getLeft())
    permutations.add(getLeftRev())
    permutations.add(getRight())
    permutations.add(getRightRev())

    for (p in permutations) p.realization.clear()


    val chain = FullChainOfStabilizers(
        26,
        base,
        permutations
    )

    println("Ans for the first part of the task is " + chain.orderOfGroup)
}







