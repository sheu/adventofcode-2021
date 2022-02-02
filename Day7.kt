package com.sheu.adventofcode

import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun main() {

    val input = File("src/main/resources/day7.txt").readLines()[0].split(",").map { it.toInt() }
    val valueToCount = input.groupingBy { it }.eachCount()
    val mostFrequentCount = valueToCount.maxByOrNull { it.value }!!.value
    val maxEntry = input.maxOrNull()!!


    println(mostFrequentCount)
    var min =  Integer.MAX_VALUE
    val allWithShortest  = valueToCount.filter { it.value == mostFrequentCount }
    for (e in 0..maxEntry){
        val  maxFuel = input.sumOf { (1..abs(e - it)).sum() }
        //println(maxFuel)
      min = minOf(min, maxFuel)
    }
    println(min)

   // val cheapestFuel = input.sumOf { abs(mostFrequent - it) }
    //println(cheapestFuel)

}
