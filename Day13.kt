package com.sheu.adventofcode

import java.io.File

fun main() {
    val input = File("src/main/resources/day13.txt").readLines().asSequence()
    val foldInstructions = input.filter { it.startsWith("fold") }.toList()
    val nonInstructionLine = input.filter { it.isNotEmpty() && !it.startsWith("fold") }
    println(nonInstructionLine.toList())
    val coordinateList = nonInstructionLine
        .map {
            val (a, b) = it.split(",")
            a.toInt() to b.toInt()
        }.toList()

    val grid = buildGrid(coordinateList)

    val result = foldInstructions.map { it.replace("fold along ", "").split("=") }
        .map { it[0] to it[1].toInt() }
        .fold(grid) {
            acc, pair ->  if(pair.first == "x") acc.foldAlongXAxis(pair.second) else acc.foldAlongYAxis(pair.second)
        }
    result.print()


}

private fun Array<Array<Char>>.foldAlongYAxis(value: Int): Array<Array<Char>> {
    val result = Array(value) { row ->
        Array(this[value].size) { col ->
            this[row][col]
        }

    }
    for (row in this.lastIndex downTo value + 1) {
        for (col in this[row].indices) {
            if (this[row][col] == '#') {
                val newX = this.lastIndex - row
                result[newX][col] = this[row][col]
            }
        }

    }
    return result
}

private fun Array<Array<Char>>.foldAlongXAxis(value: Int): Array<Array<Char>> {
    val result = Array(this.size) { row ->
        Array(value) { col ->
            this[row][col]
        }

    }
    for (row in this.indices) {
        for (col in this[row].lastIndex downTo value +1) {
            if (this[row][col] == '#') {
                val newCol = this[row].lastIndex - col

                result[row][newCol] = this[row][col]
            }
        }

    }
    return result
}

fun buildGrid(coordinates: List<Pair<Int, Int>>): Array<Array<Char>> {
    val maxCol = coordinates.maxByOrNull { it.first }!!.first + 1
    val maxRow = coordinates.maxByOrNull { it.second }!!.second + 1
    return Array(maxRow) { row ->
        Array(maxCol) { col ->
            if (col to row in coordinates) {
                '#'
            } else {
                ' '
            }
        }
    }

}

fun Array<Array<Char>>.print() {
    for (x in this.indices) {
        for (y in this[x].indices) {
            print(this[x][y])
        }
        println()
    }
}

