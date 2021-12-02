package com.sheu.adventofcode

import java.io.File

fun main() {
    val input = File("src/main/resources/day2.txt").readLines()

    val part1 = input
        .fold(Pair(0, 0)) { acc, s -> acc.applyInstruction(s) }
        .let { it.first * it.second }
    
    val part2 = input
        .fold(Triple(0, 0, 0)) { acc, s -> acc.applyInstruction(s) }
        .let { it.first * it.second }
    
    println(part1)
    println(part2)
}

fun Pair<Int, Int>.applyInstruction(instruction: String): Pair<Int, Int> {
    val (command, magnitude) = instruction.split(" ")
    return when (command) {
        "forward" -> first + magnitude.toInt() to second
        "up" -> first to second - magnitude.toInt()
        "down" -> first to second + magnitude.toInt()
        else -> error("This should never happen ($command, $magnitude)")
    }
}

fun Triple<Int, Int, Int>.applyInstruction(instruction: String): Triple<Int, Int, Int> {
    val (command, magnitude) = instruction.split(" ")
    return when (command) {
        "forward" -> Triple(first + magnitude.toInt(), second + (third * magnitude.toInt()), third)
        "up" -> Triple(first, second, third - magnitude.toInt())
        "down" -> Triple(first, second, third + magnitude.toInt())
        else -> error("This should never happen ($command, $magnitude)")
    }
}
