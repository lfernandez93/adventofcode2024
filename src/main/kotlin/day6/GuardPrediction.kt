package day6


import java.io.BufferedReader
import java.io.InputStreamReader

class GuardPrediction

fun main() {
    val map = readFile()
    val guardInitLoc = findGuardLoc(map)

    val positions = moveInMapIter(map, guardInitLoc, 0)

    //partOne
    println(
        positions.first.size
    )

    //partTwo
    findLoops(positions.second, map.map { it.toMutableList() })

}

fun findLoops(recordedPath: List<Pair<Pair<Int,Int>,Pair<Int, Int>>>, mutableMap: List<MutableList<Char>>) {
    var fromPosition = recordedPath[0]
    val loopPositions = mutableSetOf<Pair<Int,Int>>()

    for(position in recordedPath) {
        val guardPosition = position.first
        val direction = position.second
            val dx = guardPosition.first + direction.first
            val dy = guardPosition.second + direction.second
            val obstaclePosition = Pair(dx, dy)
            placeObstacle(mutableMap, obstaclePosition)
            if(canItLoop(mutableMap, fromPosition.first)) {
                loopPositions.add(obstaclePosition)
            }
            removeObstacle(mutableMap, obstaclePosition)
    }

    println(loopPositions.size)
}

fun canItLoop(mutableMap: List<MutableList<Char>>, fromPosition: Pair<Int, Int>) : Boolean {
    val directions = listOf(
        Pair(-1, 0), // North
        Pair(0, 1),  // East
        Pair(1, 0),  // South
        Pair(0, -1)  // West
    )

    val visited = mutableSetOf<Pair<Pair<Int,Int>,Pair<Int, Int>>>()
    var coords = fromPosition
    var turnCount = 0
    var directionIndex = 0

    while(true) {
        val x = coords.first
        val y = coords.second

        if(visited.contains(Pair(coords, directions[directionIndex]))) {
            return true
        } else {
            visited.add(Pair(coords, directions[directionIndex]))
        }

        val nextX = x + directions[directionIndex].first
        val nextY = y + directions[directionIndex].second

        if (nextX !in mutableMap.indices || nextY !in mutableMap[0].indices) {
            return false
        }

        if (mutableMap[nextX][nextY] != '#') {
            coords = Pair(nextX, nextY)
            turnCount = 0 // Reset turn count after a successful move
        } else {
            // Turn right by updating the direction index
            directionIndex = (directionIndex + 1) % 4
            turnCount++

            // If the guard has turned 4 times in a row, terminate to avoid infinite loops
            if (turnCount == 4) {
                return false
            }
        }
    }
}

fun placeObstacle(mutableMap: List<MutableList<Char>>, coordinates: Pair<Int, Int>) {
    val x = coordinates.first
    val y = coordinates.second
    mutableMap[x][y] = '#'
}

fun removeObstacle(mutableMap: List<MutableList<Char>>, coordinates: Pair<Int, Int>) {
    val x = coordinates.first
    val y = coordinates.second
    mutableMap[x][y] = '.'
}

fun moveInMapIter(map: List<List<Char>>, startLoc: Pair<Int, Int>, startDirectionIndex: Int): Pair<Set<Pair<Int, Int>>, List<Pair<Pair<Int,Int>,Pair<Int, Int>>>> {
    val directions = listOf(
        Pair(-1, 0), // North
        Pair(0, 1),  // East
        Pair(1, 0),  // South
        Pair(0, -1)  // West
    )

    var loc = startLoc
    var directionIndex = startDirectionIndex
    val visitedDistinct = mutableSetOf<Pair<Int, Int>>()
    var recordedPath = mutableListOf<Pair<Pair<Int,Int>,Pair<Int, Int>>>()
    var turnCount = 0

    while (true) {
        val x = loc.first
        val y = loc.second

        // Mark the current position as visited
        visitedDistinct.add(loc)

        // Determine the next position based on the current direction
        val nextX = x + directions[directionIndex].first
        val nextY = y + directions[directionIndex].second

        // Check if the guard moves out of bounds; if so, terminate
        if (nextX !in map.indices || nextY !in map[0].indices) {
            break
        }

        // If the next position is not an obstacle, move forward
        if (map[nextX][nextY] != '#') {
            recordedPath.add(Pair(loc, directions[directionIndex]))
            loc = Pair(nextX, nextY)
            turnCount = 0 // Reset turn count after a successful move
        } else {
            // Turn right by updating the direction index
            directionIndex = (directionIndex + 1) % 4
            turnCount++

            // If the guard has turned 4 times in a row, terminate to avoid infinite loops
            if (turnCount == 4) {
                break
            }
        }
    }
    return Pair(visitedDistinct, recordedPath)
}




fun findGuardLoc(map: List<List<Char>>): Pair<Int, Int> {
    for (i in map.indices) {
        for (j in map[i].indices) {
            val cell = map[i][j]
            if (cell == '^') {
                return Pair(i, j)
            }
        }
    }

    return Pair(0, 0)
}

fun readFile(): List<List<Char>> {
    val matrix = ArrayList<ArrayList<Char>>()
    //val filePath = "day6\\small_data.txt";
    val filePath = "day6\\data.txt";
    val inputStream = GuardPrediction::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            val row = ArrayList<Char>();
            for (c in line) {
                row.add(c)
            }
            matrix.add(row)

        }
    }
    return matrix
}