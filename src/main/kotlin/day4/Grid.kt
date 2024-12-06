package day4

import day3.Mul
import java.io.BufferedReader
import java.io.InputStreamReader

class Grid

fun main(args: Array<String>) {
    val xmas = "XMAS"
    val grid = readFile()
    var foundXmasCount = 0;

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == 'X') {
                // Perform star search in all 8 directions
                if (matchesXMAS(grid, i, j, 0, -1, xmas)) foundXmasCount++ // Left
                if (matchesXMAS(grid, i, j, 0, 1, xmas)) foundXmasCount++  // Right
                if (matchesXMAS(grid, i, j, -1, 0, xmas)) foundXmasCount++ // Top
                if (matchesXMAS(grid, i, j, 1, 0, xmas)) foundXmasCount++  // Bottom
                if (matchesXMAS(grid, i, j, 1, 1, xmas)) foundXmasCount++  // Bottom-right diagonal
                if (matchesXMAS(grid, i, j, 1, -1, xmas)) foundXmasCount++ // Bottom-left diagonal
                if (matchesXMAS(grid, i, j, -1, 1, xmas)) foundXmasCount++ // Top-right diagonal
                if (matchesXMAS(grid, i, j, -1, -1, xmas)) foundXmasCount++ // Top-left diagonal
            }
        }
    }

    //part 1
    println(foundXmasCount)
    foundXmasCount = 0
    //part 2
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == 'A') {
                // Check bounds for the "X-MAS" pattern
                if (i > 0 && j > 0 && i < grid.size - 1 && j < grid[i].size - 1) {
                    val topLeft = grid[i - 1][j - 1]
                    val topRight = grid[i - 1][j + 1]
                    val bottomLeft = grid[i + 1][j - 1]
                    val bottomRight = grid[i + 1][j + 1]

                    var word1 = "${topLeft}A${bottomRight}"
                    var word2 = "${topRight}A${bottomLeft}"

                    if((word1 == "MAS" && word2 == "MAS")
                        || (word1.reversed() == "MAS" && word2.reversed() == "MAS")
                        || (word1 == "MAS" && word2.reversed() == "MAS")
                        || (word1.reversed() == "MAS" && word2 == "MAS")) {
                        foundXmasCount++
                    }
                }
            }
        }
    }
    println(foundXmasCount)
}

fun matchesXMAS(grid: List<List<Char>>, i: Int, j: Int, di: Int, dj: Int, xmas: String): Boolean {
    val word = StringBuilder()
    for (k in xmas.indices) {
        val ni = i + k * di
        val nj = j + k * dj
        if (ni !in grid.indices || nj !in grid[0].indices) return false
        word.append(grid[ni][nj])
    }
    val wordStr = word.toString()
    return wordStr == xmas || wordStr.reversed() == xmas
}
fun readFile(): List<List<Char>> {
    val filePath = "day4\\data.txt"
    val inputStream = Grid::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val twoDArray = ArrayList<ArrayList<Char>>()

    reader.useLines { lines ->
        lines.forEach{ line ->
            val row = ArrayList<Char>();
            for (c in line) {
                row.add(c)
            }
            twoDArray.add(row)
            println(row)
        }
    }

    return twoDArray;
}