package com.sheu.adventofcode

import java.io.File

fun main() {
    val input  = File("src/main/resources/day1.txt").readLines()
        .asSequence()
        .map { it.toInt()}
        val part1 = input.countIncreasingDiffs()

    println(part1)
    val part2 = input.windowed(3, 1) {window -> window.sum()}
        .countIncreasingDiffs()
    println(part2)
   
}

fun Sequence<Int>.countIncreasingDiffs() = zipWithNext {a, b -> b - a }
    .filter { it > 0 }
    .count()
