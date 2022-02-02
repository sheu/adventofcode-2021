package com.sheu.adventofcode

import java.io.File
import java.util.*

fun main() {
    val input = File("src/main/resources/day10test.txt").readLines().asSequence()
        .map { it.findFirstIncorrectClosingCharacterOrNull() }.filterNotNull()
        .map {
            when (it) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> 0
            }
        }.sum()
    println(input)

    // part2
    val autoCompleted = File("src/main/resources/day10.txt").readLines().asSequence()
        .map { it.findIncompleteStringAsStack() }.filterNotNull()
        .map { it.autoComplete() }
    println(autoCompleted.toList().map { it.joinToString("") })
            val summed = autoCompleted
        .map { it.fold(0L) { acc, c -> 5 * acc + c.pointValue() } }
        .sorted().toList()
    println(summed[summed.lastIndex / 2])

}

private fun Stack<Char>.autoComplete(): List<Char> {
    val result = mutableListOf<Char>()
    val workingStack = this
    while (workingStack.isNotEmpty()) {
        val c = workingStack.pop()
        when (c) {
            '[' -> result.add(']')
            '{' -> result.add('}')
            '(' -> result.add(')')
            else -> result.add('>')
        }
    }
    return result.toList()
}

fun String.findFirstIncorrectClosingCharacterOrNull(): Char? {
    val stack = Stack<Char>()
    for (c in this) {
        if (c in listOf('{', '(', '<', '[')) {
            stack.push(c)
        } else {
            val opening = stack.pop()
            if (!opening.isOpenFor(c)) {
                return c
            }
        }
    }
    return null
}

fun String.findIncompleteStringAsStack(): Stack<Char>? {
    val stack = Stack<Char>()
    for (c in this) {
        if (c in listOf('{', '(', '<', '[')) {
            stack.push(c)
        } else {
            val opening = stack.pop()
            if (!opening.isOpenFor(c)) {
                return null
            }
        }
    }
    return stack
}

private fun Char.isOpenFor(c: Char): Boolean {
    return (this == '(' && c == ')') || (this == '[' && c == ']') || (this == '{' && c == '}') || (this == '<' && c == '>')
}

private fun Char.pointValue(): Int {
    return when (this) {
        ')' -> 1
        '}' -> 3
        ']' -> 2
        else -> 4
    }
}
