package day12

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.io.path.fileVisitor

class Garden

fun main() {
    val gardenMap = readFile()

    //gardenMap.forEach { println(it.joinToString("")) }
    val locationsVisited = mutableSetOf<Pair<Int, Int>>()
    var sum = 0L
    var sumSide = 0L
    for (i in gardenMap.indices) {
        for (j in gardenMap[i].indices) {
            val visiting = Pair(i, j)
            if(!locationsVisited.contains(visiting)) {
                val locs =  findArea(gardenMap, visiting)
                val perimeter = calculatePerimeter(locs)
                val sides = calculateSides(locs)
                sum += (locs.size * perimeter)
                sumSide += (locs.size * sides)
                locationsVisited.addAll(locs)
            }
        }
    }
    println(sum)
    println(sumSide)
}

fun calculateSides(locs: MutableSet<Pair<Int, Int>>): Int {
    if (locs.size == 1 || locs.size == 2) {
        return 4
    }

    val borders = mutableListOf<Int>()
    val borderCoords = mutableListOf<Pair<Char, Pair<Int, Int>>>()

    for(loc in locs) {
        val y = loc.first
        val x = loc.second

        val top = Pair(y - 1, x)
        val bottom = Pair(y + 1, x)

        if (!locs.contains(top)) {
            borderCoords.add(Pair('T', loc))
        }

        if (!locs.contains(bottom)) {
            borderCoords.add(Pair('B', loc))
        }

    }

    for(borderCoord in borderCoords) {
        val type = borderCoord.first
        val loc =  borderCoord.second
        val y = loc.first
        val x = loc.second

        val left = Pair(type, Pair(y, x + 1))
        val right = Pair(type, Pair(y, x - 1))

        if(!borderCoords.contains(left)) {
            borders.add(1)
        }

        if(!borderCoords.contains(right)) {
            borders.add(1)
        }
    }

    return borders.sum()
}

fun calculatePerimeter(locs: MutableSet<Pair<Int, Int>>): Int {
    val contributors = mutableListOf<Int>()
    for(loc in locs) {
        val y = loc.first
        val x = loc.second

        val top = Pair(y - 1, x)
        val bottom = Pair(y + 1, x)
        val left = Pair(y , x - 1)
        val right = Pair(y, x + 1)

        val directions = listOf(top, bottom, left, right)
        contributors.add(4 - directions.count { locs.contains(it) })
    }
    return contributors.sum()
}

fun findArea(gardenMap: List<List<Char>>, from: Pair<Int, Int>) :  MutableSet<Pair<Int, Int>>{
    val areaLocations = mutableSetOf<Pair<Int, Int>>()
    getAreas(gardenMap, mutableSetOf(), areaLocations, from)
    return areaLocations
}

fun getAreas(gardenMap: List<List<Char>>,
             locationsVisited: MutableSet<Pair<Int, Int>>,
             areaLocations: MutableSet<Pair<Int, Int>> ,
             from: Pair<Int, Int>,
             previous: Char = '_') {
    val dy = from.first
    val dx = from.second

    if(dx < 0 || dx >= gardenMap[0].size || dy < 0 || dy >= gardenMap.size || locationsVisited.contains(from)) {
        return
    }

    val visiting = gardenMap[dy][dx]
    locationsVisited.add(from)

    if(previous == visiting || previous == '_') {
        areaLocations.add(Pair(dy, dx))
        getAreas(gardenMap, locationsVisited  ,areaLocations , Pair(dy - 1, dx), visiting)
        getAreas(gardenMap, locationsVisited , areaLocations, Pair(dy + 1, dx), visiting)
        getAreas(gardenMap, locationsVisited , areaLocations, Pair(dy, dx - 1), visiting)
        getAreas(gardenMap, locationsVisited , areaLocations, Pair(dy, dx + 1), visiting)
    }else {
        return
    }
}

fun readFile() : List<List<Char>> {
    val leMap = ArrayList<List<Char>>()
    //val filePath = "day12\\small_data.txt";
    val filePath = "day12\\data.txt";
    val inputStream = Garden::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            leMap.add(line.toMutableList())
        }
    }
    return leMap
}