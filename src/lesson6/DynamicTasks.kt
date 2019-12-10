@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File
import java.lang.StringBuilder
import kotlin.math.min

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
//T=O(f*s) f=firstStr
//M=O(f*s) s=secondStr
fun longestCommonSubSequence(first: String, second: String): String {

    if (first.isEmpty() || second.isEmpty())
        return ""

    val result = StringBuilder()
    val firstStr = first.length
    val secondStr = second.length

    val matrix = Array(firstStr + 1) {
        IntArray(secondStr + 1)
    }

    var f = firstStr //strings of matrix
    var s = secondStr//columns of matrix

    for (i in 1..firstStr) {
        for (j in 1..secondStr) {
            when {
                first[i - 1] == second[j - 1] ->
                    matrix[i][j] = 1 + matrix[i - 1][j - 1]
                else ->
                    matrix[i][j] = matrix[i - 1][j]
                        .coerceAtLeast(matrix[i][j - 1])
            }
        }
    }

    while (f > 0 && s > 0)
        when {
            first[f - 1] == second[s - 1] -> {
                result.append(first[f - 1])
                f--
                s--
            }
            matrix[f][s] == matrix[f - 1][s] ->
                f--
            else ->
                s--
        }


    return result.reverse().toString()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
//T=O(n) n=list.size
//M=O(n)
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    if (list.isEmpty())
        return list

    val queues = ArrayList<MutableList<Int>>()
    var result = mutableListOf<Int>()
    var currentInt = 0
    var currentIndex = 0

    queues += mutableListOf(list[0])

    while (currentIndex < list.size) {
        currentInt = list[currentIndex]
        currentIndex++

        for (i in 0 until queues.size) {
            val filtered = queues[i]
            val previous = queues[i][queues[i].size - 1]

            if (currentInt > previous) {
                queues += filtered.toMutableList()
                filtered += currentInt
            }
        }
    }

    var maxLength = -1

    for (deque in queues.toList()) {
        if (deque.size > maxLength) {
            maxLength = deque.size
            result = deque
        }
    }

    return result
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
//T=O(c*r) c=Columns
//M=O(c*r) r=Rows
fun shortestPathOnField(inputName: String): Int {
    val input = File(inputName).readLines()
    val field = ArrayList<IntArray>()

    for (line in input) {
        field += line.split(' ')
            .map { it.toInt() }
            .toIntArray()

    }
    if (field.isEmpty())
        return 0

    val height = field.size
    val width = field[0].size


    for (i in 1 until height)
        field[i][0] += field[i - 1][0]

    for (j in 1 until width)
        field[0][j] += field[0][j - 1]


    for (i in 1 until height) {
        for (j in 1 until width) {
            field[i][j] +=
                min(field[i - 1][j], field[i][j - 1])
                    .coerceAtMost(field[i - 1][j - 1])

        }
    }

    return field[height - 1][width - 1]
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5