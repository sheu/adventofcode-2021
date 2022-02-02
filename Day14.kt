package com.sheu.adventofcode

import java.io.File

fun main() {
    val input = File("src/main/resources/day14.txt").readLines()
    val template = input.first()
    val pairToChar = input.subList(2, input.size)
        .associate {
            val (a, b) = it.split("->")
            a.trim() to b.trim()[0]
        }
    println(pairToChar)
    var result = template
    println(result)

    for (i in 0 until 10) {
        println(i)
        result = result.toCharArray().toList().zipWithNext()
            .fold("${result.first()}") { acc, pair ->
                val s = "${pair.first}${pair.second}"
                "$acc${pairToChar[s]}${pair.second}"
            }
    }
    //println(result)
    val max = result.toCharArray().toList().groupingBy {
        it
    }.eachCount()
        .entries.maxByOrNull { it.value }!!.value
    val min = result.toCharArray().toList().groupingBy {
        it
    }.eachCount()
        .entries.minByOrNull { it.value }!!.value

    println(max - min)

    // part2
    val pairToNewPair = template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    // println(pairToNewPair)
    val groupMapping = pairToChar.map { (key, value) -> key to listOf("${key[0]}$value", "$value${key[1]}") }.toMap()
    //  println(groupMapping)
    var result2 = pairToNewPair.flatMap { groupMapping.getValue(it.key) }.groupingBy { it }.eachCount().mapValues { it }
    // println(result2)
    val res = (1..40).fold(pairToNewPair) { acc, _ ->
        println(acc)
        acc.map { (atom, count) -> groupMapping[atom]!!.map { it to count } }.flatten().groupBy { it.first }
            .mapValues { (_, l) -> l.sumOf { it.second } }
    }
    println("=========RES========")
    println(res)
    val test = res.map { (k, v) -> k[0] to v }
        .groupingBy { it.first }
        .fold(0L) { acc, pair -> acc + pair.second }
    val maxPair = test.maxByOrNull { it.value }!!.toPair()
    val minPair = test.minByOrNull { it.value }!!.toPair()
    println(test)
    val finalResult = if (maxPair.first == template.last()) {
        maxPair.second + 1 - minPair.second
    } else if (minPair.first == template.last()) {
        maxPair.second - (minPair.second + 1)
    } else {
        maxPair.second - minPair.second
    }
    println(finalResult)

}
