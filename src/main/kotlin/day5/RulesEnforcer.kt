package day5

import day4.Grid
import java.io.BufferedReader
import java.io.InputStreamReader

class RulesEnforcer

fun main(args: Array<String>) {
    val theRules = readRules()
    val theUpdates = readUpdates()

    //part1
    val sumOfMids = theUpdates.filter { isValid(it, theRules) }.sumOf { it ->
        middleElement(it)
    }

    println(sumOfMids)

    //part2
    val fixedMids = theUpdates.filter { !isValid(it, theRules) }
        .map { fixIt(it, theRules) }
        .sumOf { middleElement(it) }

    println(fixedMids)
}

fun middleElement(it: List<Int>): Int {
    val mid = it.size / 2
    return if (it.size % 2 == 0) {
        it[mid - 1]
    } else {
        it[mid]
    }
}

fun isValid(updates: List<Int>, rules: Map<Int, MutableSet<Int>>): Boolean {
    var isValid = true
    for (i in updates.indices) {
        val current = updates[i]
        if (i + 1 < updates.size) {
            val next = updates[i + 1]
            if (rules[next]?.contains(current) == true) {
                isValid = false
                break
            }
        }
    }

    return isValid
}

fun fixIt(updates: List<Int>, rules: Map<Int, MutableSet<Int>>): List<Int> {
    val fixedUpdates = mutableListOf<Int>()
    fixedUpdates.addAll(updates)

    while(!isValid(fixedUpdates, rules)) {
        for (i in fixedUpdates.indices) {
            val current = fixedUpdates[i]
            if (i + 1 < fixedUpdates.size) {
                val next = fixedUpdates[i + 1]
                if (rules[next]?.contains(current) == true) {
                    fixedUpdates[i] = next
                    fixedUpdates[i + 1] = current
                }
            }
        }
    }

    return fixedUpdates
}

fun readRules(): Map<Int, MutableSet<Int>> {
    val filePath = "day5\\rules.txt";
    //val filePath = "day5\\rules_smaller.txt";
    val inputStream = RulesEnforcer::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val rules = HashMap<Int, MutableSet<Int>>()
    reader.useLines { lines ->
        lines.forEach { line ->
            val kv = line.split("|")
            val key = kv[0].toInt()
            val value = kv[1].toInt()
            var setOfRules = rules[key]
            if (setOfRules == null) {
                setOfRules = mutableSetOf()
            }
            setOfRules.add(value)
            rules[key] = setOfRules
        }
    }
    return rules

}

fun readUpdates(): List<List<Int>> {
    val filePath = "day5\\page_numbers.txt";
    //val filePath = "day5\\page_numbers_smaller.txt";
    val inputStream = RulesEnforcer::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val updates = ArrayList<List<Int>>()

    reader.useLines { lines ->
        lines.forEach { line ->
            val values = line.split(",")
                .map { it.toInt() }
                .toCollection(ArrayList())
            updates.add(values)
        }
    }

    return updates
}