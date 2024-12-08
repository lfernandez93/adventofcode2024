package day7

import java.io.BufferedReader
import java.io.InputStreamReader
enum class Op {
    ADD, MUL
}

fun generatePermutations(operators: Set<Op>, length: Int): List<List<Op>> {
    // Base case: if length is 0, return a list with an empty list
    if (length == 0) return listOf(emptyList())

    // Recursive case: build permutations by adding each operator to permutations of (length - 1)
    val smallerPermutations = generatePermutations(operators, length - 1)
    val result = mutableListOf<List<Op>>()

    for (perm in smallerPermutations) {
        for (op in operators) {
            result.add(perm + op)
        }
    }

    return result
}

class Equation
fun main() {
    val eqs = readFile()

    //part1
    val leSum = eqs.entries.filter {
        isEquationValid(it.key, it.value)
    }.sumOf {
        it.key
    }

    //println(leSum)

    val newSum = eqs.entries.filter{
        canGetToTotal(it.key, it.value)
    }.sumOf {
        it.key
    }

    println(newSum)

    var sum = 0L
    eqs.entries.forEach{
        if(isValid(it.key, it.value)) {
            sum+=it.key
        }
    }

    println(sum)
    val operators = setOf(Op.ADD, Op.MUL)
    var accum = 0L
    eqs.entries.forEach{
        val target = it.key
        val numbers = it.value
        val ops = generatePermutations(operators, numbers.size - 1)
        for (row in ops) {
            var tot = 0L
            for (i in row.indices) {
                if(i == 0) {
                    tot = numbers[0]
                }

                if(row[i] == Op.ADD) {
                    tot += numbers[i+1]
                } else {
                    tot *= numbers[i+1]
                }
            }
            if(tot == target) {
                accum += target
                break
            }
        }
    }
    println(accum)
}

fun isValid(target: Long, numbers: List<Long>) : Boolean {
    if(numbers.size == 1) {
        return numbers[0] == target
    }

    val accumSum = mutableListOf(numbers[0]+numbers[1])
    accumSum.addAll(numbers.subList(2, numbers.size))
    if(isValid(target, accumSum)) {
        return true
    }

    val accumMult = mutableListOf(numbers[0]*numbers[1])
    accumMult.addAll(numbers.subList(2, numbers.size))
    return isValid(target, accumMult)

}

fun isValidPt2(target: Long, numbers: List<Long>) : Boolean {
    if(numbers.size == 1) {
        return numbers[0] == target
    }

    val accumSum = mutableListOf(numbers[0]+numbers[1])
    accumSum.addAll(numbers.subList(2, numbers.size))
    if(isValid(target, accumSum)) {
        return true
    }

    val accumMult = mutableListOf(numbers[0]*numbers[1])
    accumMult.addAll(numbers.subList(2, numbers.size))
    if(isValid(target, accumMult)) {
        return true
    }

    val accumConcat = mutableListOf("${numbers[0]}${numbers[1]}".toLong())
    accumConcat.addAll(numbers.subList(2, numbers.size))
    return isValid(target, accumConcat)

}

fun canGetToTotal(target: Long, numbers: List<Long>) : Boolean {
    return dec(target, numbers, numbers.lastIndex)
}

//20: 10 10
//6: 3 2
//5: 3 2
fun dec(target: Long, numbers: List<Long>, ith: Int): Boolean {
    if(target <= 0) {
        return false
    }
    if(ith == 0) {
        return target == numbers[ith]
    }


    if (target % numbers[ith] == 0L && dec(target / numbers[ith], numbers, ith - 1)) {
        return true
    }

    if (target >= numbers[ith] && dec(target - numbers[ith], numbers, ith - 1)) {
        return true
    }

    return false
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
            val file = line.trim().split(": ")
            val eqResult  = file[0].toLong()
            val ops = file[1].trim().split(" ")
                .map { it.toLong() }
                .toCollection(ArrayList())
            equations[eqResult] = ops
        }
    }
    return equations
}