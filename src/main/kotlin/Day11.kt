import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day11.txt").toFile().readText()

    val stones = input.split(" ").map { it.toLong() }
    val partOne = stones.sumOf { blink(it, 25) }
    println("Part one: $partOne")

    val partTwo = stones.sumOf { blink(it, 75) }
    println("Part two: $partTwo")
}

fun blink(number: Long, blinksLeft: Int, memory: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()): Long {
    if (Pair(number, blinksLeft) in memory) return memory[Pair(number, blinksLeft)]!!

    val numberOfDigits = "$number".length
    val result = when {
        number == 0L -> listOf(1L)
        numberOfDigits % 2 == 0 -> listOf(
            "$number".substring(0 until (numberOfDigits / 2)).toLong(),
            "$number".substring(numberOfDigits / 2).toLong()
        )

        else -> listOf(number * 2024)
    }

    if (blinksLeft == 1) {
        return result.size.toLong()
    }

    val stones = result.sumOf { blink(it, blinksLeft - 1, memory) }
    memory[Pair(number, blinksLeft)] = stones

    return stones
}