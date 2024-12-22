import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day13.txt").toFile().readText()
    val games = input.split("\n\n").map { it.toDay13Game() }
    val partOne = games.sumOf { it.minTokens }
    println("Part one: $partOne")

    val games2 = games.map { it.offset() }
    val partTwo = games2.sumOf { it.minTokens }
    println("Part two: $partTwo")
}

infix fun Long.lcm(other: Long): Long {
    var multiplier = 2

    while (this * multiplier % other != 0L) {
        multiplier++
    }

    return this * multiplier
}

fun String.toDay13Game(): Day13Game {
    val (buttonA, buttonB, prize) = Regex("[^0-9]*([0-9]+)[^0-9]*([0-9]+)").findAll(this).toList()
    val (_, ax, ay) = buttonA.groupValues
    val (_, bx, by) = buttonB.groupValues
    val (_, px, py) = prize.groupValues

    return Day13Game(px.toLong(), py.toLong(), ax.toLong(), ay.toLong(), bx.toLong(), by.toLong())
}

data class Day13Game(val px: Long, val py: Long, val ax: Long, val ay: Long, val bx: Long, val by: Long) {
    private val buttonPresses: Pair<Long, Long>
        get() {
            val lcm = ax lcm ay
            val multiplierX = lcm / ax
            val multiplierY = lcm / ay

            val pxMul = px * multiplierX
            val pyMul = py * multiplierY

            val bxMul = bx * multiplierX
            val byMul = by * multiplierY

            val dp = pxMul - pyMul
            val db = bxMul - byMul

            val b = dp / db
            val a = (px - (bx * b)) / ax

            return Pair(a, b)
        }

    private val buttonPressesValid get() = px == buttonPresses.first * ax + buttonPresses.second * bx && py == buttonPresses.first * ay + buttonPresses.second * by

    val minTokens get() = if (buttonPressesValid) buttonPresses.first * 3 + buttonPresses.second else 0

    fun offset() = Day13Game(
        px + 10000000000000,
        py + 10000000000000,
        ax,
        ay,
        bx,
        by
    )
}