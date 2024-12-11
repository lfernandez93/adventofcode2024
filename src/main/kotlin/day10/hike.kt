package day10

import java.io.BufferedReader
import java.io.InputStreamReader

class Hike

fun main() {

    val map = readFile()
    val startingPoints = searchStartingPoints(map)

    //pt1
    var sumOf = startingPoints.sumOf {
        val nines = mutableSetOf<Pair<Int, Int>>()
        searchPath(map, it.second , it.first, 0, true, nines)
        nines.size
    }
    println(sumOf)

    //pt2
    sumOf = startingPoints.sumOf {
        val nines = mutableListOf<Pair<Int, Int>>()
        searchDistinctPath(map, it.second , it.first, 0, true, nines)
        nines.size
    }
    println(sumOf)
}

fun searchStartingPoints(map: List<List<Int>>): List<Pair<Int, Int>> {
    var startingPoints = ArrayList<Pair<Int, Int>>()

    for (i in map.indices) {
        for (j in map[i].indices) {
            if(map[i][j] == 0) {
                startingPoints.add(Pair(i, j))
            }
        }
    }

    return startingPoints
}

fun searchPath(map: List<List<Int>>, dx: Int, dy: Int,
               previous: Int,
               beginning: Boolean = false,
               pathVisited: MutableSet<Pair<Int, Int>>) {
    if((dy >= map.size || dy < 0)  || (dx >= map[0].size || dx < 0)) {
        return
    }

    val visiting = map[dy][dx]

    if (previous + 1 == visiting || beginning)  {
        if(visiting == 9) {
            pathVisited.add(Pair(dx, dy))
            return
        }
        searchPath(map, dx, dy - 1, visiting, pathVisited = pathVisited)
        searchPath(map, dx, dy + 1, visiting, pathVisited = pathVisited)
        searchPath(map, dx + 1, dy, visiting, pathVisited = pathVisited)
        searchPath(map, dx - 1, dy, visiting, pathVisited = pathVisited)
    }

}


fun searchDistinctPath(map: List<List<Int>>, dx: Int, dy: Int,
               previous: Int,
               beginning: Boolean = false,
               pathVisited: MutableList<Pair<Int, Int>>) {
    if((dy >= map.size || dy < 0)  || (dx >= map[0].size || dx < 0)) {
        return
    }

    val visiting = map[dy][dx]

    if (previous + 1 == visiting || beginning)  {
        if(visiting == 9) {
            pathVisited.add(Pair(dx, dy))
            return
        }

        searchDistinctPath(map, dx, dy - 1, visiting, pathVisited = pathVisited)
        searchDistinctPath(map, dx, dy + 1, visiting, pathVisited = pathVisited)
        searchDistinctPath(map, dx + 1, dy, visiting, pathVisited = pathVisited)
        searchDistinctPath(map, dx - 1, dy, visiting, pathVisited = pathVisited)
    }

}

fun readFile() : List<List<Int>> {
    val leMap = ArrayList<List<Int>>()
    //val filePath = "day10\\small_data.txt";
    val filePath = "day10\\data.txt";
    val inputStream = Hike::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            leMap.add(line.map { it.digitToInt() }.toList())
        }
    }
    return leMap
}