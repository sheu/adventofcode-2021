package com.sheu.adventofcode

import com.sheu.adventofcode.ConsoleColors.RESET
import com.sheu.adventofcode.ConsoleColors.WHITE_BOLD_BRIGHT
import java.io.File
import java.lang.Math.pow
import java.util.*
import kotlin.math.*

fun main() {
    val input = File("src/main/resources/day15.txt").readLines()
    val grid = input.map { it.toCharArray().map { c -> "$c".toInt() } }.expandByFactor(5)
   //println(grid)
    val initMap = grid.buildInitialMap()
   // println(initMap)

   val path = dijkstra(initMap, grid, 0 to 0)
    println(path)
    val totalCost = path.dropLast(1).fold(0){acc, pair -> acc+grid[pair.first][pair.second]}
    println(totalCost)


}

fun List<List<Int>>.getNeighbours(row: Int, col: Int): List<Pair<Int, Int>> {

    val neighbours = mutableListOf<Pair<Int, Int>>()
    if (row < lastIndex)
        neighbours.add(row + 1 to col)
    if (row > 0) {
        neighbours.add(row - 1 to col)
    }
    if (col < this[row].lastIndex)
        neighbours.add(row to col + 1)
    if (col > 0) {
        neighbours.add(row to col - 1)
    }
    return neighbours
}

data class Node(val parent: Pair<Int, Int>?, val value: Int, val totalCost: Int, val current: Pair<Int, Int>, val heuristic: Int, val visited: Boolean = false, val inQueue: Boolean = false)

fun List<List<Int>>.buildInitialMap(): Map<Pair<Int, Int>, Node> {
    val initialPointToNode = mutableMapOf<Pair<Int, Int>, Node>()
    forEachIndexed { row, columnList ->
        columnList.forEachIndexed { col, value ->
            initialPointToNode[row to col] =
                Node(null, value,Integer.MAX_VALUE, row to col, 0)
        }
    }
    return initialPointToNode
}

fun dijkstra(
    workingMap: Map<Pair<Int, Int>, Node>,
    grid: List<List<Int>>,
    startPoint: Pair<Int, Int>
): List<Pair<Int, Int>> {
    val tempWorkingMap = workingMap.toMutableMap()
    tempWorkingMap[startPoint] = Node(null, grid[0][0],0, startPoint, 0)

    val riskComparator: Comparator<Node> = compareBy { it.totalCost + it.heuristic }
    val costQueue = PriorityQueue(riskComparator)


    costQueue.add(Node(null, grid[0][0], 0, startPoint, 0))
    var count = 0
    mainLoop@ while (costQueue.isNotEmpty()) {
        count++
        val currentNode = costQueue.remove()
        tempWorkingMap[currentNode.current] = tempWorkingMap[currentNode.current]!!.copy( visited = true)
        val neighbours = grid.getNeighbours(currentNode.current.first, currentNode.current.second)
        neighbours.forEach {
            if (!(tempWorkingMap[it]!!.visited)) {
                val newNode = Node(
                    currentNode.current,
                    currentNode.value,
                    if (currentNode.totalCost == Integer.MAX_VALUE) 0 + grid[it.first][it.second] else currentNode.totalCost + grid[it.first][it.second],
                    it,
                    0
                )
                tempWorkingMap.compute(it) { _, v ->
                    if (newNode.totalCost < v!!.totalCost) {
                        newNode
                    } else {
                        v
                    }
                }
                if(!tempWorkingMap[it]!!.inQueue) {
                    costQueue.add(tempWorkingMap[it])
                    tempWorkingMap[it] = tempWorkingMap[it]!!.copy(inQueue = true)
                }
            }
        }
        if (currentNode.current == grid.lastIndex to grid.lastIndex) break@mainLoop

    }
    printGrid(tempWorkingMap, grid)
    val path = mutableListOf<Pair<Int, Int>>()
    var current = tempWorkingMap[grid.lastIndex to grid.lastIndex]!!
    path.add(current.current)
    while (current.parent != null) {
        path.add(current.parent!!)
        current = tempWorkingMap[current.parent]!!
    }
    println(tempWorkingMap[path.first()])
    return path

}

fun List<List<Int>>.expandByFactor(factor: Int): List<List<Int>> {
    val tempArray = Array(size*factor){Array(size*factor){0} }
    // fill new created array with initial grid
    forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, col ->
            tempArray[rowIndex][colIndex] = col
        }
    }
    // fill the first n rows
    for(row in 0 until size) {
        for (i in this.first().size..tempArray.lastIndex step size) {
            for (col in 0 until size) {
                val newValue  = tempArray[row][i-size + col]+1
                tempArray[row][i+col] = if(newValue > 9) 1 else newValue
            }
        }
    }

    //fill the rest
    for (col in 0..tempArray.lastIndex) {
        for (i in size..tempArray.lastIndex step size) {
            for (row in 0 until size) {
                val newValue  = tempArray[i-size + row][col]+1
                tempArray[row+i][col] = if(newValue > 9) 1 else newValue
            }
        }
    }
    return tempArray.toList().map { it.toList() }
}

fun printGrid(map: Map<Pair<Int, Int>, Node>, grid: List<List<Int>>) {
    for(i in 0..grid.lastIndex) {
        for(j in 0..grid.lastIndex) {
            if(map[i to j]!!.visited) {
                val printValue = "$WHITE_BOLD_BRIGHT${grid[i][j]}$RESET"
                print(printValue)
            } else print(grid[i][j])
        }
        println()
    }
}

object ConsoleColors {
    // Reset
    const val RESET = "\u001b[0m" // Text Reset

    // Regular Colors
    const val BLACK = "\u001b[0;30m" // BLACK
    const val RED = "\u001b[0;31m" // RED
    const val GREEN = "\u001b[0;32m" // GREEN
    const val YELLOW = "\u001b[0;33m" // YELLOW
    const val BLUE = "\u001b[0;34m" // BLUE
    const val PURPLE = "\u001b[0;35m" // PURPLE
    const val CYAN = "\u001b[0;36m" // CYAN
    const val WHITE = "\u001b[0;37m" // WHITE

    // Bold
    const val BLACK_BOLD = "\u001b[1;30m" // BLACK
    const val RED_BOLD = "\u001b[1;31m" // RED
    const val GREEN_BOLD = "\u001b[1;32m" // GREEN
    const val YELLOW_BOLD = "\u001b[1;33m" // YELLOW
    const val BLUE_BOLD = "\u001b[1;34m" // BLUE
    const val PURPLE_BOLD = "\u001b[1;35m" // PURPLE
    const val CYAN_BOLD = "\u001b[1;36m" // CYAN
    const val WHITE_BOLD = "\u001b[1;37m" // WHITE

    // Underline
    const val BLACK_UNDERLINED = "\u001b[4;30m" // BLACK
    const val RED_UNDERLINED = "\u001b[4;31m" // RED
    const val GREEN_UNDERLINED = "\u001b[4;32m" // GREEN
    const val YELLOW_UNDERLINED = "\u001b[4;33m" // YELLOW
    const val BLUE_UNDERLINED = "\u001b[4;34m" // BLUE
    const val PURPLE_UNDERLINED = "\u001b[4;35m" // PURPLE
    const val CYAN_UNDERLINED = "\u001b[4;36m" // CYAN
    const val WHITE_UNDERLINED = "\u001b[4;37m" // WHITE

    // Background
    const val BLACK_BACKGROUND = "\u001b[40m" // BLACK
    const val RED_BACKGROUND = "\u001b[41m" // RED
    const val GREEN_BACKGROUND = "\u001b[42m" // GREEN
    const val YELLOW_BACKGROUND = "\u001b[43m" // YELLOW
    const val BLUE_BACKGROUND = "\u001b[44m" // BLUE
    const val PURPLE_BACKGROUND = "\u001b[45m" // PURPLE
    const val CYAN_BACKGROUND = "\u001b[46m" // CYAN
    const val WHITE_BACKGROUND = "\u001b[47m" // WHITE

    // High Intensity
    const val BLACK_BRIGHT = "\u001b[0;90m" // BLACK
    const val RED_BRIGHT = "\u001b[0;91m" // RED
    const val GREEN_BRIGHT = "\u001b[0;92m" // GREEN
    const val YELLOW_BRIGHT = "\u001b[0;93m" // YELLOW
    const val BLUE_BRIGHT = "\u001b[0;94m" // BLUE
    const val PURPLE_BRIGHT = "\u001b[0;95m" // PURPLE
    const val CYAN_BRIGHT = "\u001b[0;96m" // CYAN
    const val WHITE_BRIGHT = "\u001b[0;97m" // WHITE

    // Bold High Intensity
    const val BLACK_BOLD_BRIGHT = "\u001b[1;90m" // BLACK
    const val RED_BOLD_BRIGHT = "\u001b[1;91m" // RED
    const val GREEN_BOLD_BRIGHT = "\u001b[1;92m" // GREEN
    const val YELLOW_BOLD_BRIGHT = "\u001b[1;93m" // YELLOW
    const val BLUE_BOLD_BRIGHT = "\u001b[1;94m" // BLUE
    const val PURPLE_BOLD_BRIGHT = "\u001b[1;95m" // PURPLE
    const val CYAN_BOLD_BRIGHT = "\u001b[1;96m" // CYAN
    const val WHITE_BOLD_BRIGHT = "\u001b[1;97m" // WHITE

    // High Intensity backgrounds
    const val BLACK_BACKGROUND_BRIGHT = "\u001b[0;100m" // BLACK
    const val RED_BACKGROUND_BRIGHT = "\u001b[0;101m" // RED
    const val GREEN_BACKGROUND_BRIGHT = "\u001b[0;102m" // GREEN
    const val YELLOW_BACKGROUND_BRIGHT = "\u001b[0;103m" // YELLOW
    const val BLUE_BACKGROUND_BRIGHT = "\u001b[0;104m" // BLUE
    const val PURPLE_BACKGROUND_BRIGHT = "\u001b[0;105m" // PURPLE
    const val CYAN_BACKGROUND_BRIGHT = "\u001b[0;106m" // CYAN
    const val WHITE_BACKGROUND_BRIGHT = "\u001b[0;107m" // WHITE
}




