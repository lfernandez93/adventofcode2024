package day14

import day13.Claw
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.abs

data class RoboLoc(var origin: Pair<Int, Int>, val velocity: Pair<Int, Int>)
fun main() {
    val roboLocs = readFiles()
    val wide = 101
    val tall = 103
    //val leMap = buildMap(11, 7)
    val locations = roboLocs.map { roboMove(it, wide, tall, 0) }
        .toCollection(mutableListOf())

    val skipWide = wide / 2
    val skipTall = tall /2

    val locsToCompute = locations.filter { it.first != skipWide}
        .filter { it.second != skipTall }
    val quadrants = buildQuadrants(locsToCompute, skipWide, skipTall)

    val product = quadrants.values.reduce{ v, k -> v * k}
    println(product)

}

fun buildQuadrants(locs: List<Pair<Int, Int>>, midWide: Int, midTall: Int) : MutableMap<Int, Int> {
    val quadrants = mutableMapOf<Int, Int>()
    locs.forEach {
        val x = it.first
        val y = it.second

        if(x < midWide && y < midTall) {
            quadrants[0] = if(quadrants[0] != null) quadrants[0]!! + 1  else 1
        } else if(x > midWide && y < midTall) {
            quadrants[1] = if(quadrants[1] != null) quadrants[1]!! + 1  else 1
        } else if(x < midWide && y > midTall) {
            quadrants[2] = if(quadrants[2] != null) quadrants[2]!! + 1  else 1
        } else if (x > midWide && y > midTall) {
            quadrants[3] = if(quadrants[3] != null) quadrants[3]!! + 1  else 1
        }
    }
    return quadrants
}

fun placeRobot(map: MutableList<MutableList<Char>>, loc: Pair<Int, Int>) {
    val x = loc.first
    val y = loc.second
    val oldVal = map[y][x]
    if(oldVal == '.') {
        map[y][x] = '1'
    } else {
        map[y][x] = (oldVal.digitToInt() + 1).digitToChar()
    }
}

fun roboMove(loc: RoboLoc, wide: Int, tall: Int, secs: Int): Pair<Int, Int> {
    if(secs >= 100) {
        return loc.origin
    }
    val x = loc.origin.first
    val y = loc.origin.second
    val dx = loc.velocity.first
    val dy = loc.velocity.second

    var newX = x + dx
    var newY = y + dy

    if(newX > wide - 1 || newX < 0) {
        newX = wide - (abs(x + dx))
        if(newX < 0 ) {
            newX = abs(newX)
        }
    }

    if(newY > tall - 1 || newY < 0) {
        newY = tall - (abs(y + dy))
        if(newY < 0 ) {
            newY = abs(newY)
        }
    }

    loc.origin = Pair(newX, newY)
    return roboMove(loc, wide, tall, secs + 1)
}



fun buildMap(x: Int, y: Int) : MutableList<MutableList<Char>> {
    val map = mutableListOf<MutableList<Char>>()
    for(i in 0..< y) {
        val rows = mutableListOf<Char>()
        for (j in 0..< x) {
            rows.add('.')
        }
        map.add(rows)
    }
    return map
}

fun drawMap(map: MutableList<MutableList<Char>>) {
    map.forEach {
        println(it.joinToString(""))
    }
}

fun readFiles() : List<RoboLoc> {
    val robo = mutableListOf<RoboLoc>()
    //val filePath = "day14\\small_data.txt";
    val filePath = "day14\\data.txt";
    val inputStream = RoboLoc::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            val div = line.split(" ")
            val og = div[0].split("=")[1].split(",")
            val vel = div[1].split("=")[1].split(",")
            robo.add(RoboLoc(Pair(og[0].toInt(), og[1].toInt()), Pair(vel[0].toInt(), vel[1].toInt())))
        }
    }

    return robo
}