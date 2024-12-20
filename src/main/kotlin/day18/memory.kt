package day18


import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

data class Memory(val position: Pair<Int, Int>, var accum: Int)

fun main() {
    val memLocs = readFile()
    val map = initMap(70)
    placeMemory(memLocs.subList(0, 1024), map)
    drawMap(map)
    travel(map, Pair(0, 0))
    val newMap = initMap(70)
    travelTillCant(newMap, memLocs)
}

fun travel(map: MutableList<MutableList<Char>>, loc: Pair<Int, Int>) : Boolean {
    val directions = listOf(
        Pair(-1, 0),
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1)
    )

    val toVisit: Queue<Memory> = LinkedList()
    toVisit.add(Memory(loc, 0))
    val locToCost = mutableMapOf<Pair<Int, Int>, Int>()
    var minCost = Int.MAX_VALUE
    var reachedEnd = false

    while(!toVisit.isEmpty()) {
        val visiting = toVisit.poll()
        val currentLoc = visiting.position
        val x = currentLoc.second
        val y = currentLoc.first
        val accum = visiting.accum

        if (x < 0 || x >= map.size
            || y < 0 || y >= map.size
            || map[y][x] == '#'
            || accum > minCost
        ) {
            continue
        }

        val cost = locToCost[currentLoc]
        if(cost != null) {
            if(cost > accum) {
                locToCost[currentLoc] = accum
            } else {
                continue
            }
        } else {
            locToCost[currentLoc] = accum
        }

        if (x == map.size - 1 && y == map.size - 1) {
            if(minCost > accum) {
                minCost = accum
                reachedEnd = true
            }
            continue
        }

        for(dir in directions) {
            toVisit.add(Memory(Pair(y + dir.first, x + dir.second), accum + 1))
        }
    }

    //println(minCost)

    return reachedEnd
}

fun travelTillCant(map: MutableList<MutableList<Char>>, memLocs: List<Pair<Int, Int>>) {
    for(memLoc in memLocs) {
        placeMemory(memLoc, map)
        val reachedEnd = travel(map, Pair(0, 0))
        if(!reachedEnd) {
            println("Oh no $memLoc")
            break
        }
    }
}
fun drawMap(map: MutableList<MutableList<Char>>) {
    map.forEach { println(it.joinToString("")) }
}

fun placeMemory(memLocs: List<Pair<Int, Int>>, map: MutableList<MutableList<Char>>) {
    memLocs.forEach { map[it.first][it.second] = '#' }
}

fun placeMemory(memLoc: Pair<Int, Int>, map: MutableList<MutableList<Char>>) {
    map[memLoc.first][memLoc.second] = '#'
}

fun initMap(size: Int) : MutableList<MutableList<Char>> {
    val map = mutableListOf<MutableList<Char>>()
    for(i in 0..size) {
        val row = mutableListOf<Char>()
        for(j in 0 .. size) {
            row.add('.')
        }
        map.add(row)
    }
    return map
}
fun readFile(): List<Pair<Int, Int>> {
    val memLocs = mutableListOf<Pair<Int, Int>>()
    val filePath = "day18\\data.txt";
    //val filePath = "day18\\small_data.txt";
    val inputStream = Memory::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            val coords = line.split(",")
            memLocs.add(Pair(coords[1].toInt(), coords[0].toInt()))
        }


        return memLocs
    }
}