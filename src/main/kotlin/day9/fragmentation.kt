package day9

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Stack

class Fragmentation

fun main() {
    //val input = readFile()
    val input = "2333133121414131402".map { it.digitToInt() }.toCollection(ArrayList())
    var count = 0
    var inputCount = 0
    //explode
    val exploded = input.joinToString(
        separator = "",
        transform = {
            val isEven = inputCount % 2 == 0
            var space = ""
            if (it > 0) {
                for (i in 0..<it) {
                    space += if (isEven) "$count" else "."
                }
            }
            inputCount++
            if (isEven) count++
            space
        }
    )
    //println(exploded)

    //compact
    val mutableExploded = exploded.toMutableList()
    var stackOfNumbers = buildStackOfNumberPositions(mutableExploded)
    var lastNPos = stackOfNumbers.pop()
    for ((ith, c) in mutableExploded.withIndex()) {
        if (c == '.' && ith < lastNPos) {
            val toSwap = mutableExploded[lastNPos]
            val dot = mutableExploded[ith]
            mutableExploded[ith] = toSwap
            mutableExploded[lastNPos] = dot
            lastNPos = stackOfNumbers.pop()
            //println(mutableExploded.joinToString(separator = ""))
        } else if (ith >= lastNPos) {
            break
        }

    }
    var pos = 0
    val checkSum = mutableExploded
        .filter { it.isDigit() }
        .sumOf {
            pos++ * it.digitToInt()
        }
    println(checkSum)
}

fun buildStackOfNumberPositions(str: MutableList<Char>): Stack<Int> {
    val stackOfPositions = Stack<Int>()
    for ((counter, char) in str.withIndex()) {
        if (char.isDigit()) {
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