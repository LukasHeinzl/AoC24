import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day8.txt").toFile().readText()
    val lines = input.split("\n")
    val maxX = lines[0].length
    val maxY = lines.size
    val antennas = lines
        .mapIndexed { y, row -> row.mapIndexed { x, col -> Day8Antenna(x, y, col) }.filter { it.frequency != '.' } }
        .flatten()

    val partOneCount =
        getAntiNodes(antennas, maxX, maxY).count { it.first in 0 until maxX && it.second in 0 until maxY }
    println("Part one: $partOneCount")

    val partTwoCount = getAntiNodes(antennas, maxX, maxY, true).count()
    println("Part two: $partTwoCount")
}

data class Day8Antenna(val x: Int, val y: Int, val frequency: Char)

fun getAntiNodes(antennas: List<Day8Antenna>, maxX: Int, maxY: Int, partTwo: Boolean = false) =
    antennas.map { antenna ->
        val matches = antennas.filter { it.frequency == antenna.frequency && it != antenna }
        matches.map {
            val distance = Pair(it.x - antenna.x, it.y - antenna.y)

            if (partTwo) {
                var currentPosition = Pair(antenna.x, antenna.y)
                val positions = mutableListOf<Pair<Int, Int>>()

                while (currentPosition.first in 0 until maxX && currentPosition.second in 0 until maxY) {
                    positions += currentPosition
                    currentPosition += distance
                }

                positions
            } else {
                listOf(
                    Pair(antenna.x - distance.first, antenna.y - distance.second),
                    Pair(it.x + distance.first, it.y + distance.second)
                )
            }
        }.flatten()
    }.flatten().distinct()