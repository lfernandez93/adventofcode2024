package day17

import day16.Maze
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.pow

data class Input(val register: MutableMap<Char, Long>, val instructions: List<Int>)
data class PartialResult(val output: Int, val isSkip: Boolean)
fun main() {
    val input = readFile()
    println(input)
    val instructions = input.instructions
    var results = mutableListOf<Int>()
    var i = 0
    while(i <= instructions.size - 2) {
        val instruction = instructions[i]
        val op = instructions[i + 1]
        val pr = execute(instruction, op, input.register)
        if(pr.output != -1 && !pr.isSkip) {
            results.add(pr.output)
        }
        i = if(pr.isSkip) {
            pr.output
        } else {
            i+2
        }
    }
    println(results.joinToString(","))
}

fun execute(instruction: Int, operand: Int, register: MutableMap<Char, Long>): PartialResult {
    val registerA = register['A']
    val registerB = register['B']
    val registerC = register['C']
    var partialResult = PartialResult(-1, false)
    when(instruction) {
        0 -> {
            register['A'] = (registerA!!
                .div(2.00.pow(getCombo(register, operand).toDouble()))).toLong()
        }
        1 -> {
            register['B'] = registerB!! xor operand.toLong()
        }
        2 ->{
            register['B'] = getCombo(register, operand).mod(8).toLong()
        }
        3 -> {
            if(registerA != 0L) {
                partialResult = PartialResult(operand, true)
            }
        }
        4 -> {
            register['B'] = registerB!! xor registerC!!
        }
        5 -> {
            val str = getCombo(register, operand).mod(8)
            partialResult = PartialResult(str, false)
        }
        6 -> {
            register['B'] = (registerA!!
                .div(2.00.pow(getCombo(register, operand).toDouble()))).toLong()
        }
        7 -> {
            register['C'] = (registerA!!
                .div(2.00.pow(getCombo(register, operand).toDouble()))).toLong()
        }
    }

    return partialResult
}

fun getCombo(register: MutableMap<Char, Long>, operand: Int) : Long {
    var combo = operand.toLong()
     if (operand == 4) {
        combo = register['A']!!.toLong()
    } else if (operand == 5) {
        combo = register['B']!!.toLong()
    } else if (operand == 6) {
        combo = register['C']!!.toLong()
    }

    return combo
}

fun readFile() : Input {
    val filePath = "day17\\data.txt";
    //val filePath = "day17\\small_data.txt";
    val register = mutableMapOf<Char, Long>()
    val instructions = mutableListOf<Int>()
    //val filePath = "day17\\small_data.txt";
    val inputStream = Input::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    reader.useLines { lines ->

        lines.forEach { line ->
            if (line.startsWith("Register")) {
               val divided = line.split(":")
                register.put(
                    divided[0].replace("Register ", "").first(),
                    divided[1].trim().toLong()
                )
            } else if (line.startsWith("Program")) {
                val divided = line.split(":")
                val insStr = divided[1].trim()
                insStr.split(",").map { it.toInt() }.toCollection(instructions)
            }
        }
    }

    return Input(register, instructions)
}