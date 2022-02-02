package com.sheu.adventofcode

import java.io.File

fun main() {

    val input = File("src/main/resources/day8test.txt").readLines()
        .map { it.split("|")[1].trim() }.map { it.split(" ") }.flatten()
    println(input)
    val result = input.count { it.length in listOf(2, 3, 4, 7) }
    println(result)

    // part2

    val input2 = File("src/main/resources/day8.txt").readLines().map {

       val (i, o) = it.split("|")
        i.trim().split(" ") to o.trim().split(" ")
    }.map { determineDigits(it.first, it.second) }
    println(input2)
    println(input2.sum())


}

fun determineDigits(input: List<String>, output: List<String>) : Int {
    val stringToDigit = createMapping(input)
    println("$input -> $stringToDigit")
    return output.map {
        println(it)
        stringToDigit[it.toList().sorted().joinToString("")]!!
    }.joinToString("").toInt()
}

fun createMapping(input: List<String>): Map<String, Int> {
    val uniqueDigits = input.filter { it.length in  listOf(2, 3, 4, 7)}
    val zeroSixNine = input.filter {it.length == 6}.map { it.toSet() }
    val twoThreeFive = input.filter { it.length == 5 }.map { it.toSet() }
    val digitToString = uniqueDigits.associate {
        when (it.length) {
            2 -> 1 to it
            3 ->  7 to it
            4 -> 4 to it
            else -> 8 to it
        }
    }.toMutableMap()
    val nine = zeroSixNine.first {  it.containsAll(digitToString[4]!!.toSet())}.joinToString("")
    digitToString[9] = nine
    val zero = zeroSixNine.first {  it.containsAll(digitToString[7]!!.toSet()) && !it.containsAll(digitToString[4]!!.toSet())}.joinToString("")
    digitToString[0] = zero
    val six = input.first { it.length == 6 && it != nine && it != zero }
    digitToString[6] = six
    val three = twoThreeFive.first {  it.containsAll(digitToString[1]!!.toSet())}.joinToString("")
    digitToString[3] = three
    val five = twoThreeFive.first {  it.intersect(digitToString[4]!!.toSet()).size == 3 && it.joinToString("") != three}.joinToString("")
    digitToString[5] = five
    val two  =  input.first{it !in digitToString.values}
    digitToString[2] = two

    return digitToString.map { e -> e.value to e.key }
        .associate { it.first.toList().sorted().joinToString("") to it.second }

}
