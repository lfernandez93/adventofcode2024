package day2

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.abs

class Levels

fun main(args: Array<String>) {
    val extractedLevels = readFile();

    //calculate whether is safe or not for part 1
    var safeReports = extractedLevels.count { isSafe(it.levels) }
    println("isSafe= $safeReports")

    //calculate from the ones that are unsafe which ones can be safe if we remove one
    var unsafeReportsNowSafe = extractedLevels.filter { !isSafe(it.levels) }
        .count { isSafeWithRemoval(it.levels) }

    println("safeWithRemoval = $unsafeReportsNowSafe")

    //total
    println("total = ${safeReports + unsafeReportsNowSafe}")
}

fun readFile(): List<ExtractedLevels> {
    val filePath = "day2\\data.txt"
    val inputStream = Levels::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val allLevels = ArrayList<ExtractedLevels>();

    reader.useLines { lines ->
        lines.forEach { line ->
            val regex = "\\s".toRegex()
            val split = line.split(regex)
            allLevels.add(ExtractedLevels(split.map { it.toInt() }.toCollection(ArrayList())))
        }
    }

    return allLevels;
}

fun isLevelIncreasingOrDecreasing(levels: List<Int>): Boolean {
    var isDescending = false;
    var isAscending = false;

    for (i in 0..levels.size - 2) {
        val current = levels[i];
        val next = levels[i + 1];
        if (current > next) {
            isDescending = true;
        } else {
            isDescending = false
            break
        }
    }

    for (i in 0..levels.size - 2) {
        val current = levels[i];
        val next = levels[i + 1];
        if (current < next) {
            isAscending = true;
        } else {
            isAscending = false
            break
        }
    }

    return isDescending || isAscending;
}

fun isWithinThreeRange(levels: List<Int>): Boolean {
    var isWithinThreeRange = true;
    for (i in 0..levels.size - 2) {
        val current = levels[i];
        val next = levels[i + 1];

        if (abs(current - next) > 3) {
            isWithinThreeRange = false;
            break;
        }
    }

    return isWithinThreeRange;
}

fun isSafe(levels: List<Int>): Boolean {
    return isLevelIncreasingOrDecreasing(levels) && isWithinThreeRange(levels)
}

fun isSafeWithRemoval(levels: List<Int>): Boolean {
    var isSafeWithRemoval = false;
    for (i in levels.indices) {
        val minusOneLevelArray = ArrayList<Int>();
        for (j in levels.indices) {
            if (i != j) {
                minusOneLevelArray.add(levels[j])
            }
        }

        if (isSafe(minusOneLevelArray)) {
            isSafeWithRemoval = true;
            break;
        }
    }

    return isSafeWithRemoval;
}