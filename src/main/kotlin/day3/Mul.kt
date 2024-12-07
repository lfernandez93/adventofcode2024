package day3

import day2.ExtractedLevels
import day2.Levels
import java.io.BufferedReader
import java.io.InputStreamReader

class Mul

fun main(args: Array<String>) {
    val lines = readFile()
    //part 1
    //extract the muls per line
    //multiply each of lines and sum
    //sum everything
    val sumOfMul = lines.map(::extractMuls).sumOf { i -> i.map(::multiply).sum() }
    println(sumOfMul)

    //part 2
    //make all the text be a string
    //then remove any occurrences of don'ts only select things between dos
    //and the very beginning
    //repeat some of the stuff we did for part 1
    val allText = lines.joinToString("");
    var cleansed = constructValidStringForMul(allText)
    println(
        extractMuls(cleansed).map(::multiply).sum()
    )

}

fun readFile(): List<String> {
    val filePath = "day3\\rules.txt"
    val inputStream = Mul::class.java.classLoader.getResourceAsStream(filePath)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val linesArray = ArrayList<String>()
    reader.useLines { lines ->
        lines.forEach(linesArray::add)
    }

    return linesArray;
}

fun constructValidStringForMul(corruptedString: String) : String {
    var cleanedString = corruptedString.substring(0, corruptedString.indexOf("don\'t"))
    var restOfCorruptedString = corruptedString.substring(corruptedString.indexOf("don\'t"), corruptedString.length - 1)
    val regex = Regex("do\\(\\)|don\\'t\\(\\)")
    var closed = false
    var from : Int? = 0
    var to : Int?
    regex.findAll(restOfCorruptedString)
        .forEach { it ->
            it.groups.forEach { it ->
                    if(it?.value.equals("do()") && !closed) {
                        from = it?.range?.last
                        closed = true;
                    } else if (closed && it?.value.equals("don\'t()")){
                        to = it?.range?.first
                        cleanedString +=(from?.let { it1 -> to
                            ?.let { it2 -> restOfCorruptedString.substring(it1, it2) } })
                        closed = false;
                    }
                }
            }

    return cleanedString;
}


fun extractMuls(line: String): List<String> {
    val regex = Regex("mul\\(\\d*\\,\\d*\\)")
    return regex.findAll(line).map { it.value
        .replace("mul(", "")
        .replace(")", "") }
        .toCollection(ArrayList())
}

fun multiply(item: String) : Int {
    var digits = item.split(",")
    return digits[0].toInt() * digits[1].toInt()
}
