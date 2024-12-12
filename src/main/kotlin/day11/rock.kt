package day11

data class RockComp(val level: Int, var value: String)

fun main() {
    val data = "2 77706 5847 9258441 0 741 883933 12".split(" ").toList()
    val blinkTimes = 75
    var blinkCount = 0
    var composedRock = mutableMapOf<RockComp, Long>()

    for(s in data) {
        val rock = RockComp(0, s)

        if(composedRock[rock] == null) {
            composedRock[rock] = 1L
        } else {
            composedRock[rock]?.plus(1L)
        }
    }

    while (blinkCount < blinkTimes) {
        val middleRepresentation = mutableMapOf<RockComp, Long>()
        for (entry in composedRock.entries) {
            val rockComp = entry.key
            val number = rockComp.value
            val occurrence = entry.value
            if (number == "0") {
                val newRockComp = RockComp(blinkCount + 1, "")
                newRockComp.value = "1"
                middleRepresentation[newRockComp] = (middleRepresentation[newRockComp] ?: 0) + occurrence
            } else if (number.length % 2 == 0) {
                val left = removeLeftZeroes(number.substring(0, number.length / 2))
                val right = removeLeftZeroes(number.substring(number.length / 2))
                val leftRockComp = RockComp(blinkCount + 1, left)
                val rightRockComp = RockComp(blinkCount + 1, right)
                middleRepresentation[leftRockComp] = (middleRepresentation[leftRockComp] ?: 0) + occurrence
                middleRepresentation[rightRockComp] = (middleRepresentation[rightRockComp] ?: 0) + occurrence
            } else {
                val newRockComp = RockComp(blinkCount + 1, ((number.toLong() * 2024).toString()))
                middleRepresentation[newRockComp] = (middleRepresentation[newRockComp] ?: 0) + occurrence
            }
        }

        composedRock = middleRepresentation

        if (blinkCount == blinkTimes - 1) {
            println(composedRock.values.sum())
        }

        blinkCount++
    }


}

fun removeLeftZeroes(value: String): String {
    var removed = value
    if (value.length > 1 && value.first() == '0') {
        removed = value.toInt().toString()
    }
    return removed
}