package day15


import java.io.BufferedReader
import java.io.InputStreamReader

data class Input(val map: MutableList<MutableList<Char>>, val movements: List<Char>)

fun main() {
    val input = readFile()
    val movements = input.movements
    val map = input.map
    var roboLoc = locateRoboLoc(map)
    for(movement in movements) {
        val newLoc = startMovement(roboLoc, map, movement)
        //drawMap(map)
        //println(movement)
        roboLoc = newLoc
    }

    val boxes = findBoxes(map)
    var mult = 0
    boxes.forEach {
        mult+= (100 * (it.first +1) + (it.second + 1))
    }
    println(mult)
}

fun findBoxes(map: MutableList<MutableList<Char>>): List<Pair<Int, Int>> {
    val boxCoordinates = mutableListOf<Pair<Int, Int>>()
    for(i in map.indices) {
        for(j in map[i].indices) {
            if (map[i][j] == 'O') {
                boxCoordinates.add(Pair(i, j))
            }
        }
    }
    return boxCoordinates
}

fun startMovement(roboLoc: Pair<Int, Int>, map: MutableList<MutableList<Char>>, movement: Char): Pair<Int, Int> {
    val delta = getDeltaByMovement(movement)
    val x = roboLoc.second
    val y = roboLoc.first
    val dx = delta.second
    val dy = delta.first
    val newX = x + dx
    val newY = y + dy

    if (newX < 0 || newX >= map[0].size || newY < 0 || newY >= map.size) {
        return roboLoc
    }

    val newPositionObject = map[newY][newX]

    if (newPositionObject == '#') {
        return roboLoc
    } else if (newPositionObject == '.') {
        map[y][x] = '.'
        map[newY][newX] = '@'

        return Pair(newY, newX)
    } else if (newPositionObject == 'O') {
        val newPos = Pair(newY, newX)
        return if(moveBox(delta, map, newPos)) {
            map[y][x] = '.'
            map[newY][newX] = '@'
            newPos
        } else {
            roboLoc
        }
    }


    return roboLoc
}

fun drawMap(map: MutableList<MutableList<Char>>) {
    println()
    println()
    println()
    println()
    println()
    println()
    for (i in map.indices) {
        println(map[i].joinToString(""))

    }
}

fun moveBox(direction: Pair<Int, Int>, map: MutableList<MutableList<Char>>, currentLoc: Pair<Int, Int> ) : Boolean {
    val x = currentLoc.second
    val y = currentLoc.first
    val dx = direction.second
    val dy = direction.first
    val newX = x + dx
    val newY = y + dy

    if (newX < 0 || newX >= map[0].size || newY < 0 || newY >= map.size) {
        return false
    }

    val newPositionObject = map[newY][newX]

    if (newPositionObject == '#') {
        return false
    } else if (newPositionObject == '.') {
        map[y][x] = '.'
        map[newY][newX] = 'O'
        return true
    } else if (newPositionObject == 'O') {
        if(moveBox(direction, map, Pair(newY, newX))) {
            map[y][x] = '.'
            map[newY][newX] = 'O'
            return true
        } else {
            return false
        }
    }

    return false
}

fun getDeltaByMovement(movement: Char): Pair<Int, Int> {
    var delta = Pair(-1, -1)
    when (movement) {
        '^' -> delta = Pair(-1, 0)
        'v' -> delta = Pair(1, 0)
        '>' -> delta = Pair(0, 1)
        '<' -> delta = Pair(0, -1)
    }
    return delta
}

fun locateRoboLoc(map: List<List<Char>>): Pair<Int, Int> {
    var roboLoc = Pair(-1, -1)
    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j] == '@') {
                roboLoc = Pair(i, j)
                break
            }
        }
    }
    return roboLoc
}

fun readFile(): Input {
    val movements = mutableListOf<Char>()
    val map = mutableListOf<MutableList<Char>>()
    var row = mutableListOf<Char>()

    val filePath = "day15\\data.txt";
    //val filePath = "day15\\small_data.txt";
    val inputStream = Input::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            if (!line.startsWith("######")) {
                if (line.startsWith("#")) {
                    var counter = 0
                    for (char in line) {
                        if ((counter == 0 || counter == line.length - 1) && char == '#') {
                            counter++
                            continue
                        }
                        row.add(char)
                        counter++
                    }
                    //println(row.joinToString(""))
                    map.add(row)
                    row = mutableListOf()
                } else if (line.isNotBlank()) {
                    for (char in line) {
                        movements.add(char)
                    }
                }
            }

        }
    }

    return Input(map, movements)

}