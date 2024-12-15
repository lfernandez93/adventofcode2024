package day13

import day12.Garden
import java.io.BufferedReader
import java.io.InputStreamReader

data class Claw(val buttonA: Pair<Long, Long>, val buttonB: Pair<Long, Long>, var locPrize: Pair<Long, Long>)

fun main() {
    val claws = readFile()
    val error = 10000000000000
    claws.map {
        val locPrize = it.locPrize
        val newLocPrize = Pair(locPrize.first + error, locPrize.second + error)
        it.locPrize = newLocPrize
    }

    println(claws.sumOf { solvePt2(it) })
}

fun solve(claw: Claw) : Int {
    var minCost = Int.MAX_VALUE
    var feasible = false

    for(i in 0..100) {
        for(j in 0 .. 100) {
            val buttonA = claw.buttonA
            val buttonB = claw.buttonB
            val xDelta = (buttonA.first * i) + buttonB.first * j
            val yDelta = (buttonA.second * i) + buttonB.second * j

            val prize = claw.locPrize

            if(xDelta == prize.first && yDelta == prize.second) {
                val cost = (3 * i) + (1 * j)
                feasible = true
                minCost = if (cost <= minCost) cost else minCost
            }
        }

    }

    minCost = if (feasible) minCost else 0

    return minCost
}

fun solvePt2(claw: Claw) : Long {
    val buttonA = claw.buttonA
    val buttonB = claw.buttonB
    val prize = claw.locPrize

    val det = (buttonA.first * buttonB.second) - (buttonA.second * buttonB.first)

    if (det == 0L) {
        return 0
    }

    val dtma = (prize.first * buttonB.second) - (prize.second * buttonB.first)

    val dtmb = (prize.second * buttonA.first) - (prize.first * buttonA.second)

    if (dtma % det != 0L || dtmb % det != 0L) {
        return 0
    }

    val a = dtma / det
    val b = dtmb / det

    if (a < 0 || b < 0) {
        return 0
    }

    return (3 * a) + (1 * b)
}

fun readFile(): List<Claw> {
    val claws = mutableListOf<Claw>()
    var count = 0
    val div = 3
    //val filePath = "day13\\small_data.txt";
    val filePath = "day13\\data.txt";
    val inputStream = Claw::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    var keeper = mutableListOf<Pair<Long, Long>>()
    reader.useLines { lines ->
        lines.forEach { line ->
            if(line.isNotBlank()) {
                val lineType = count.mod(div)
                val movementString = line.split(":")[1].split(",")
                var x = 0L
                var y = 0L

                if(lineType <= 1) {
                     x = movementString[0].split("+")[1].toLong()
                     y = movementString[1].split("+")[1].toLong()
                } else {
                    x = movementString[0].split("=")[1].toLong()
                    y =  movementString[1].split("=")[1].toLong()
                }

                if (lineType <= 2) {
                    keeper.add(Pair(x, y))
                }

                if (lineType == 2) {
                    claws.add(Claw(keeper[0], keeper[1], keeper[2]))
                    keeper = mutableListOf()
                }

                count++
            }
        }
    }
    return claws
}