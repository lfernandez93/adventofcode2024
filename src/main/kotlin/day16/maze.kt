package day16

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

data class Maze(val pos: Pair<Int, Int>, val currDir: Pair<Int, Int>, var accum: Int)

fun main() {
    val map = readFile()
    val startLoc = findStart(map)

    move(startLoc, map)
}

fun move(startLoc: Pair<Int, Int>, map: MutableList<MutableList<Char>>) {
    val directions = listOf(
        Pair(-1, 0),
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1)
    )

    val toVistit : Queue<Maze> = LinkedList()
    toVistit.add(Maze(startLoc, directions[1], 0))
    val leLocs = mutableMapOf<Pair<Int, Int>, Int>()

    while(!toVistit.isEmpty()) {
        val currVisit = toVistit.poll()
        val loc = currVisit.pos
        val x = loc.second
        val y = loc.first
        val currLocVal = map[y][x]
        val accum = currVisit.accum
        val currDir = currVisit.currDir

        if(currLocVal == '#') {
            continue
        }

        val cost = leLocs[loc]

        if(cost != null) {
            if(cost > accum) {
                leLocs[loc] = accum
            } else {
                continue
            }
        } else {
            leLocs[loc] = accum
        }

        if(currLocVal == 'E') {
            continue
        }

        for(dir in directions) {
            val sofar = if (dir != currDir) accum + 1001 else accum + 1
            toVistit.add(Maze(Pair(loc.first + dir.first, loc.second + dir.second), dir, sofar))
        }

    }

    println(leLocs[findEnd(map)])
}

fun readFile(): MutableList<MutableList<Char>> {
    val map = mutableListOf<MutableList<Char>>()
    val filePath = "day16\\data.txt";
    //val filePath = "day16\\small_data.txt";
    val inputStream = Maze::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->

        lines.forEach { line ->
            val row = mutableListOf<Char>()
            var counter = 0
            for (char in line) {
                row.add(char)
                counter++

            }
            map.add(row)
        }


        return map
    }
}

fun findStart(map: MutableList<MutableList<Char>>) : Pair<Int, Int> {
    var start = Pair(0, 0)
    for(i in map.indices) {
        for(j in map[i].indices) {
            if (map[i][j] == 'S') {
                start = Pair(i, j)
            }
        }
    }
    return start
}

fun findEnd(map: MutableList<MutableList<Char>>) : Pair<Int, Int> {
    var start = Pair(0, 0)
    for(i in map.indices) {
        for(j in map[i].indices) {
            if (map[i][j] == 'E') {
                start = Pair(i, j)
            }
        }
    }
    return start
}