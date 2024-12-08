package day8

import day7.Equation
import java.io.BufferedReader
import java.io.InputStreamReader

class Antennas

fun main() {
    val antennasMap = readFile()
    drawMap(antennasMap)
    println("part1")
    println("_________________________")
    println()
    val frequencies = findFrequenciesPositions(antennasMap)
    val antinodesPositions = calculateAntinodesPositions(frequencies, antennasMap[0].size, antennasMap.size)
    var mutableAntennasMap = antennasMap.map { it.toMutableList() }
    placeAntinodes(antinodesPositions, mutableAntennasMap)
    println()
    println("_________________________")
    println()
    drawMap(mutableAntennasMap)
    println()
    println("_________________________")
    println()
    println("antinodes=${antinodesPositions.size}")
    println()
    println("_________________________")
    println("part2")
    println("_________________________")
    mutableAntennasMap = antennasMap.map { it.toMutableList() }
    val allAntinodesPositions = calculateAntinodesPositionsAcross(frequencies, antennasMap[0].size, antennasMap.size)
    placeAntinodes(allAntinodesPositions, mutableAntennasMap)
    drawMap(mutableAntennasMap)
    println("_________________________")
    println()
    println("allAntinodes=${allAntinodesPositions.size}")


}

fun placeAntinodes(antinodes: Set<Pair<Int, Int>>, mutableMap: List<MutableList<Char>>) {
    for (i in mutableMap.indices) {
        for (j in mutableMap[i].indices) {
            val loc = Pair(i, j)

            if(antinodes.contains(loc)) {
                mutableMap[i][j] = '#'
            }
        }
    }
}

fun calculateAntinodesPositions(positions: Map<Char, List<Pair<Int, Int>>>, xMax: Int, yMax: Int): Set<Pair<Int, Int>> {
    val antinodePositions = mutableSetOf<Pair<Int, Int>>()

    positions.entries.forEach{
        var freqPositions = it.value
        for (i in freqPositions.indices) {
            for (j in freqPositions.indices) {
                if(i != j) {
                    val posA = freqPositions[i]
                    val posB = freqPositions[j]
                    println("$posA = $posB")
                    val dx = posA.first - posB.first
                    val dy = posA.second - posB.second
                    println("${dx},${dy}")

                    val antinodeXPos = posA.first + dx
                    val antinodeYPos = posA.second + dy

                    if( (antinodeXPos in 0..<xMax) && (antinodeYPos in 0..<yMax) ) {
                        antinodePositions.add(Pair(antinodeXPos, antinodeYPos))
                    }
                }
            }
        }
    }

    return antinodePositions
}

fun calculateAntinodesPositionsAcross(positions: Map<Char, List<Pair<Int, Int>>>, xMax: Int, yMax: Int): Set<Pair<Int, Int>> {
    val antinodePositions = mutableSetOf<Pair<Int, Int>>()

    positions.entries.forEach{
        var freqPositions = it.value
        for (i in freqPositions.indices) {
            for (j in freqPositions.indices) {
                if(i != j) {
                    val posA = freqPositions[i]
                    val posB = freqPositions[j]
                    antinodePositions.add(posA)
                    println("$posA = $posB")
                    val dx = posA.first - posB.first
                    val dy = posA.second - posB.second
                    println("${dx},${dy}")
                    var antinodeXPos = posA.first + dx
                    var antinodeYPos = posA.second + dy

                    do{
                        if ((antinodeXPos in 0..<xMax) && (antinodeYPos in 0..<yMax)) {
                            antinodePositions.add(Pair(antinodeXPos, antinodeYPos))
                        }
                        antinodeXPos += dx
                        antinodeYPos += dy
                    }while ((antinodeXPos in 0..<xMax) && (antinodeYPos in 0..<yMax))
                }
            }
        }
    }

    return antinodePositions
}


fun findFrequenciesPositions(antennasMap: List<List<Char>>) : Map<Char, List<Pair<Int, Int>>> {
    val frequenciesPositions = HashMap<Char, ArrayList<Pair<Int, Int>>>()

    for(i in antennasMap.indices) {
        for(j in antennasMap[i].indices) {
            val cell = antennasMap[i][j]
            if(cell.isLetterOrDigit()) {
                var positions = frequenciesPositions[cell]
                if(positions == null) {
                    positions = ArrayList()
                    frequenciesPositions[cell] = positions
                }
                positions.add(Pair(i, j))
            }
        }
    }

    return frequenciesPositions
}

fun drawMap(map: List<List<Char>>) {
    map.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
}

fun readFile() : List<List<Char>> {
    val antennaMap = ArrayList<ArrayList<Char>>()
    //val filePath = "day8\\small_data.txt";
    val filePath = "day8\\data.txt";
    val inputStream = Antennas::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            val row = ArrayList<Char>()
            for (c in line) {
                row.add(c)
            }
            antennaMap.add(row)
        }
    }
    return antennaMap
}