package day7

import java.io.BufferedReader
import java.io.InputStreamReader

class Equation
fun main() {
    val eqs = readFile()

    //part1
    val leSum = eqs.entries.filter {
        isEquationValid(it.key, it.value)
    }.sumOf {
        it.key
    }

    println(leSum)

    val newSum = eqs.entries.filter{
        canGetToTotal(it.key, it.value)
    }.sumOf {
        it.key
    }

    println(newSum)


}

fun canGetToTotal(target: Long, numbers: List<Long>) : Boolean {
    return dec(target, numbers, numbers.lastIndex)
}

//20: 10 10
//6: 3 2
//5: 3 2
fun dec(target: Long, numbers: List<Long>, ith: Int): Boolean {
    if(ith == 0) {
        return target == numbers[ith]
    }
    //3769341064121
    if(target.mod(numbers[ith]) == 0L) {
        if (dec(target / numbers[ith], numbers, ith - 1)) {
            return true
        }
    }

    return dec(target - numbers[ith], numbers, ith - 1)
}

fun isEquationValid(target: Long, numbers: List<Long>): Boolean{
    return joinNumbers(1, numbers[0] , target, numbers)
}

fun joinNumbers(ith: Int, currentValue: Long, target: Long, numbers: List<Long>): Boolean {
    if(ith == numbers.size) {
        return currentValue == target
    }

    if(joinNumbers(ith + 1, currentValue * numbers[ith], target, numbers)) {
        return true
    }

    return joinNumbers(ith + 1, currentValue + numbers[ith], target, numbers)
}
fun readFile(): Map<Long,List<Long>> {
    val equations = HashMap<Long,List<Long>>()
    //val filePath = "day7\\small_data.txt";
    val filePath = "day7\\data.txt";
    val inputStream = Equation::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->
        lines.forEach { line ->
            var file = line.split(": ")
            var eqResult  = file[0].toLong()
            var ops = file[1].trim().split(" ")
                .map { it.toLong() }
                .toCollection(ArrayList())
            equations[eqResult] = ops
        }
    }
    return equations
}