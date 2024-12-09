package day9

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Stack

class Fragmentation

fun main() {
    val input = readFile()
    var idCount = 0
    var index = 0

    //explode
    val exploded = ArrayList<String>()
    input.forEach {
        val isEven = index % 2 == 0
        for (i in 0..<it) {
            exploded.add(if (isEven) "$idCount" else ".")
        }
        index++
        if (isEven) idCount++
    }

    //compact
    val mutableExploded = exploded.toMutableList()
    val stackOfNumbers = buildStackOfNumberPositions(mutableExploded)
    var lastNPos = stackOfNumbers.pop()
    for ((ith, c) in mutableExploded.withIndex()) {
        if (c == "." && ith < lastNPos) {
            val toSwap = mutableExploded[lastNPos]
            val dot = mutableExploded[ith]
            mutableExploded[ith] = toSwap
            mutableExploded[lastNPos] = dot
            lastNPos = stackOfNumbers.pop()
        }
    }

    var checksum = 0L
    for ((counter, char) in mutableExploded.withIndex()) {
        if ("." != char) {
            checksum += (counter * char.toInt())
        } else {
            break
        }
    }

    println(checksum)

}

fun buildStackOfNumberPositions(str: MutableList<String>): Stack<Int> {
    val stackOfPositions = Stack<Int>()
    for ((counter, chain) in str.withIndex()) {
        if ("." != chain) {
            stackOfPositions.add(counter)
        }
    }
    return stackOfPositions
}

fun readFile(): List<Int> {
    val input = ArrayList<Int>()
    val filePath = "day9\\data.txt";
    val inputStream = Fragmentation::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            line.map { it.digitToInt() }.toCollection(input)
        }
    }
    return input
}