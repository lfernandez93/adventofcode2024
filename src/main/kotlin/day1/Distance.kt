package day1

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.LinkedList
import kotlin.math.abs

class DanceApplication

fun main(args: Array<String>) {
    //read file
    val extractedLists = readFile()
    //sort
    val sortedLeftList = extractedLists.leftList.sorted()
    val sortedRightList = extractedLists.rightList.sorted()

    //calculate distances
    val distances = calculateDistances(sortedLeftList, sortedRightList)

    //sum all
    println(distances.sum())
}

fun readFile(): ExtractedLists {
    val filePath = "day1\\data.txt"
    val inputStream = DanceApplication::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val leftList = ArrayList<Int>();
    val rightList = ArrayList<Int>();

    reader.useLines { lines ->
        lines.forEach { line ->
            val regex = "\\s{3}".toRegex()
            val split = line.split(regex)
            leftList.add(split[0].toInt())
            rightList.add(split[1].toInt())
        }
    }

    return ExtractedLists(leftList, rightList)
}

fun calculateDistances(sortedLeftList: List<Int>, sortedRightList: List<Int>): List<Int> {
    val distances = ArrayList<Int>();
    for (i in sortedLeftList.indices) {
        val leftSideValue = sortedLeftList[i]
        val rightSideValue = sortedRightList[i]
        distances.add(abs(leftSideValue - rightSideValue))
    }
    return distances;
}