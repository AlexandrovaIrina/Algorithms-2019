@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.lang.Math.*


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
// Оценка алгоритма
// время работы - О(n * m)
// ресурсоемкость - O(n * m)
// n - длина первой строки
// m - длина второй строки
fun longestCommonSubSequence(first: String, second: String): String {

    val firstLength = first.length
    val secondLength = second.length
    var subLength = Array ( firstLength + 1) { Array (secondLength + 1) {0} }

    for (i in 0 until firstLength) {
        for (j in 0 until secondLength) {
            if (first[i] == second[j])
                subLength[i + 1][j + 1] = subLength[i][j] + 1
            else
                subLength[i + 1][j + 1] = max(subLength[i + 1][j], subLength[i][j + 1])
        }
    }

    var answer = StringBuilder (max (firstLength, secondLength))
    var itFirst = firstLength
    var itSecond = secondLength

    while (itFirst > 0 && itSecond > 0) {
        if (first[itFirst - 1] == second[itSecond - 1]) {
            answer.append(first[itFirst - 1])
            itFirst --
            itSecond --
        }
        else if (subLength[itFirst - 1][itSecond] > subLength[itFirst][itSecond - 1])
            itFirst --
        else
            itSecond --
    }
    return answer.reverse().toString()
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
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    val size = list.size
    if (list.isEmpty() || size == 1) return list
    var longestSeq = Array(size) {0}
    var indexes = Array(size) {0}

    for (i in 0 until size) {
        longestSeq[i] = 1
        indexes[i] = -1
        for (j in 0 until i) {
            if (list[j] < list[i] && longestSeq[j] + 1 > longestSeq[i]) {
                longestSeq[i] = longestSeq[j] + 1
                indexes[i] = j
            }
        }
    }

    var maxLength = longestSeq[0]
    var pos = 0

    for (i in 0 until size) {
        if (longestSeq[i] > maxLength){
            maxLength = longestSeq[i]
            pos = i
        }
    }

    var order = Array (maxLength) {0}
    var len = 0

    while (pos != -1) {
        order[len] = pos
        pos = indexes[pos]
        len ++
    }
    var result = listOf <Int> ()
    for (i in order.size - 1 downTo 0){
        result += list[order[i]]
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
fun shortestPathOnField(inputName: String): Int {
    TODO()
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5