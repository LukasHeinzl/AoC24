import java.nio.file.Paths
import kotlin.math.abs

fun main() {
    val input = Paths.get("src/main/resources/day10.txt").toFile().readText()
    val locations = input.split("\n").mapIndexed { y, row ->
        row.mapIndexed { x, col -> Day10Location(x, y, col - '0') }
    }.flatten()

    locations.forEach { it.findNeighbors(locations) }
    val partOneCount = locations.filter { it.height == 0 }.sumOf { countPeaks(it) }
    println("Part one: $partOneCount")

    val partTwoCount = locations.filter { it.height == 0 }.sumOf { countPeaks(it, false) }
    println("Part one: $partTwoCount")
}

fun countPeaks(start: Day10Location, distinct: Boolean = true): Int {
    val queue = mutableListOf(start)
    val peaks = mutableListOf<Pair<Int, Int>>()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val currentPair = Pair(current.x, current.y)

        if (current.height == 9) peaks += currentPair

        current.neighbors.filter {
            it.height > current.height
        }.forEach { queue += it }
    }

    return if (distinct) peaks.distinct().size else peaks.size
}

data class Day10Location(
    val x: Int,
    val y: Int,
    val height: Int,
    val neighbors: MutableList<Day10Location> = mutableListOf()
) {
    fun findNeighbors(locations: List<Day10Location>) {
        neighbors.addAll(locations.filter {
            ((abs(it.x - x) == 1 && it.y - y == 0) || (it.x - x == 0 && abs(it.y - y) == 1)) &&
                    abs(it.height - height) <= 1
        })
    }
}