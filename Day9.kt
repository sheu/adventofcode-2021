package com.sheu.adventofcode

import java.io.File

fun main() {
    val lowPoints = mutableListOf<Pair<Pair<Int, Int>, Int>>()
    val input = File("src/main/resources/day9.txt").readLines()
        .map { it.toList().map { c -> "$c".toInt() } }
     input
        .forEachIndexed { row, outer ->
            outer.forEachIndexed { column, inner ->
                if (row == 0) {
                    if (column == 0) {
                        if (inner < outer[column + 1] && inner < input[row + 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    } else if (column == outer.lastIndex) {
                        if (inner < outer[column - 1] && inner < input[row + 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    } else {
                        if (inner < outer[column - 1] && inner < outer[column + 1] && inner < input[row + 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    }
                } else if (row == input.lastIndex) {
                    if (column == 0) {
                        if (inner < outer[column + 1] && inner < input[row - 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    } else if (column == outer.lastIndex) {
                        if (inner < outer[column - 1] && inner < input[row - 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    } else {
                        if (inner < outer[column - 1] && inner < outer[column + 1] && inner < input[row - 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    }
                } else {
                    if (column == 0) {
                        if (inner < outer[column + 1] && inner < input[row + 1][column] && inner < input[row - 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    } else if (column == outer.lastIndex) {
                        if (inner < outer[column - 1] && inner < input[row + 1][column] && inner < input[row - 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    } else {
                        if (inner < outer[column - 1] && inner < outer[column + 1] && inner < input[row + 1][column] && inner < input[row - 1][column]) {
                            lowPoints.add((row to column) to inner)
                        }
                    }
                }
            }
        }
    println(lowPoints)
    println(lowPoints.sumOf { it.second + 1 })
    val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
    val basinValue = lowPoints.map { basin(it.first, null, input.lastIndex, input.first().lastIndex, input, visited) }
    println(basinValue.sortedDescending().subList(0, 3).reduce { acc, i -> acc*i  })


}

fun basin(point: Pair<Int, Int>, prev: Int?, maxX: Int, maxY: Int, grid: List<List<Int>>, visited: MutableSet<Pair<Int, Int>>): Int {
    if(point in visited) return 0
    if(point.first < 0 || point.second < 0) return 0
    if(point.first > maxX || point.second > maxY) return 0
    if(grid[point.first][point.second] == 9 ) return 0
    if(prev != null) {
        if (grid[point.first][point.second] <= prev) return 0
        if ((prev - grid[point.first][point.second]) > 1 ) return 0
    }
    val currentValue = grid[point.first][point.second]
    visited.add(point)
    println("Adding : $currentValue")
    return 1 + basin(point.first + 1 to point.second, currentValue, maxX, maxY, grid, visited) +
            basin(point.first - 1 to point.second, currentValue, maxX, maxY, grid, visited) +
            basin(point.first to point.second - 1, currentValue, maxX, maxY, grid, visited) +
            basin(point.first to point.second + 1, currentValue, maxX, maxY, grid, visited)
}

