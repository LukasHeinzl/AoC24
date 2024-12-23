import java.nio.file.Paths
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {
    val input = Paths.get("src/main/resources/day18.txt").toFile().readText()
    val size = 70
    val bytes = input.split("\n").map {
        val (x, y) = it.split(",")
        Pair(x.toInt(), y.toInt())
    }

    val partOneSteps = aStar(Pair(0, 0), Pair(size, size), bytes.take(1024)).size - 1
    println("Part one: $partOneSteps")

    var numberOfBytes = 1025

    while (aStar(Pair(0, 0), Pair(size, size), bytes.take(numberOfBytes)).isNotEmpty()) {
        numberOfBytes++
    }

    println("Part two: ${bytes[numberOfBytes - 1]}")
}

fun Pair<Int, Int>.getDistanceTo(other: Pair<Int, Int>): Int {
    val dx = first - other.first.toDouble()
    val dy = second - other.second
    return sqrt(dx * dx + dy * dy).roundToInt()
}

fun Pair<Int, Int>.getNeighbors(): List<Pair<Int, Int>> = listOf(
    Pair(first - 1, second),
    Pair(first + 1, second),
    Pair(first, second - 1),
    Pair(first, second + 1)
)

fun aStar(start: Pair<Int, Int>, end: Pair<Int, Int>, blocked: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
    val nodeQueue = mutableListOf(start)
    val backtrackMap = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>(start to null)
    val visited = mutableListOf<Pair<Int, Int>>()
    val startDistMap = mutableMapOf(start to 0)
    val totalDistMap = mutableMapOf(start to start.getDistanceTo(end))

    while (nodeQueue.isNotEmpty()) {
        val n = nodeQueue.minBy { totalDistMap[it]!! }

        if (n == end) {
            val path = mutableListOf<Pair<Int, Int>>()
            var current: Pair<Int, Int>? = n

            while (current != null) {
                path += current
                current = backtrackMap[current]
            }

            return path
        }

        nodeQueue.remove(n)
        visited.add(n)

        for (neighbor in n.getNeighbors()) {
            if (neighbor.first !in 0..end.first || neighbor.second !in 0..end.second) continue

            if (neighbor in visited) {
                continue
            }

            if (neighbor !in nodeQueue && neighbor !in blocked) {
                nodeQueue += neighbor
            }

            val dist = startDistMap[n]!! + n.getDistanceTo(neighbor)

            if (startDistMap.containsKey(neighbor) && dist >= startDistMap[neighbor]!!) {
                continue
            }

            backtrackMap[neighbor] = n
            startDistMap[neighbor] = dist
            totalDistMap[neighbor] = dist + neighbor.getDistanceTo(end)
        }
    }

    return listOf()
}