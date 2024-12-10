package day9

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Stack

class Fragmentation

fun main() {
    val input = readFile()
    //val input = "2333133121414131402".map { it.digitToInt() }.toList()
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

    //compact pt1
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

    val mutableExplodedForPt2 = exploded.toMutableList()
    val files = buildMetadata(mutableExplodedForPt2)
    val sortedFiles = files.sortedByDescending { it.first.toInt() }
    var freeSpaces = buildMetadata(mutableExplodedForPt2, free = true)

    for (file in sortedFiles) {
        val id = file.first
        var filePos = file.second
        val fileOccurrence = file.third
        val freeSpace = findFreePosition(file, freeSpaces)
        if(freeSpace != null) {
            var dotPos = freeSpace.second
            for (i in 0 ..< fileOccurrence) {
                mutableExplodedForPt2[dotPos] = id
                mutableExplodedForPt2[filePos] = "."
                dotPos++
                filePos++
            }
            freeSpaces = buildMetadata(mutableExplodedForPt2, free = true)
        }
    }

    //println(mutableExplodedForPt2.joinToString(""))

    checksum = 0L
    for ((counter, char) in mutableExplodedForPt2.withIndex()) {
        if ("." != char) {
            checksum += (counter * char.toInt())
        }
    }
    println(checksum)


}

fun findFreePosition(file: Triple<String, Int, Int>, freePositions: List<Triple<String, Int, Int>>): Triple<String, Int, Int>? {
    var fileOccurrence = file.third
    var filePosition = file.second
    return freePositions.firstOrNull { filePosition > it.second   && fileOccurrence <=  it.third }
}


fun buildMetadata(str: MutableList<String>, free: Boolean = false): List<Triple<String, Int, Int>> {
    val metadataStack = ArrayList<Triple<String, Int, Int>>()
    var incidenceCount = 1
    var startPosition = 0
    for ((counter, current) in str.withIndex()) {
        var isReset = false
        if(!free && current == ".") continue
        if(free && current != ".") continue
        if (counter < str.lastIndex) {
            if(current == str[counter + 1]) {
                if (incidenceCount == 1) {
                    startPosition = counter
                }
                incidenceCount++
            } else {
               isReset = true
            }
        } else {
            isReset = true
        }

        if(isReset) {
            if (incidenceCount == 1 && startPosition == 0) {
                metadataStack.add(Triple(current, counter, 1))
            } else {
                metadataStack.add(Triple(current, startPosition, incidenceCount))
            }
            incidenceCount = 1
            startPosition = 0
        }
    }
    return metadataStack
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