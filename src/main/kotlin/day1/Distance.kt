package day1

import java.io.BufferedReader
import java.io.InputStreamReader
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

    //result of first part
    println(distances.sum())

    //transform the right list into an incidence map.
    val incidenceMap = toIncidenceMap(sortedRightList);

    //calculate the similarity
    val similarities = calculateSimilarities(sortedLeftList, incidenceMap)

    //result similarity score, second part
    println(similarities.sum())
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

fun toIncidenceMap(rightList: List<Int>) : Map<Int, Int> {
    val incidenceMap = HashMap<Int, Int>();
    rightList.forEach { i ->
        val incidence = incidenceMap[i]
        if (incidence == null) {
            incidenceMap[i] = 1;
        } else {
            incidenceMap[i] = incidence + 1
        }
    }
    return incidenceMap
}

fun calculateSimilarities(sortedLeftList: List<Int>, incidenceMap: Map<Int, Int>): List<Int> {
    val  similarities = ArrayList<Int>()
    sortedLeftList.forEach { i ->
        val multiplier = incidenceMap[i] ?: 0
        similarities.add(i * multiplier)
    }
    return similarities
}