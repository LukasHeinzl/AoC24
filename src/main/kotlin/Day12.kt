import java.nio.file.Paths
import kotlin.math.abs

fun main() {
    val input = Paths.get("src/main/resources/day12.txt").toFile().readText()
    val input2 = "AAAA\n" +
            "BBCD\n" +
            "BBCC\n" +
            "EEEC"
    val input3 = "OOOOO\n" +
            "OXOXO\n" +
            "OOOOO\n" +
            "OXOXO\n" +
            "OOOOO"
    val input4 = "RRRRIICCFF\n" +
            "RRRRIICCCF\n" +
            "VVRRRCCFFF\n" +
            "VVRCCCJFFF\n" +
            "VVVVCJJCFE\n" +
            "VVIVCCJJEE\n" +
            "VVIIICJJEE\n" +
            "MIIIIIJJEE\n" +
            "MIIISIJEEE\n" +
            "MMMISSJEEE"
    val plots = input.split("\n").mapIndexed { y, row -> row.mapIndexed { x, col -> Day12Plot(x, y, col) } }.flatten()
    plots.forEach { it.findNeighbors(plots) }
    plots.forEach { it.generateUniqueId() }

    val partOne = plots.distinctBy { it.uniqueId }.sumOf { it.totalArea * it.totalPerimeter }
    println("Part one: $partOne")
}

data class Day12Plot(
    val x: Int,
    val y: Int,
    val type: Char,
    val connectedPlots: MutableList<Day12Plot> = mutableListOf()
) {
    var uniqueId: Int? = null
    private val perimeter get() = 4 - connectedPlots.size
    private val corners: Int
        get() {
            val outerCorners = when (connectedPlots.size) {
                1 -> 2
                2 -> if (connectedPlots.all { it.x == x } || connectedPlots.all { it.y == y }) 0 else 1
                else -> 0
            }

            val innerCorners = if(connectedPlots.size > 1){
                1
            }else 0

            return outerCorners
        }

    private val allPlots: List<Day12Plot> by lazy {
        val allPlots = mutableListOf(this)
        val queue = connectedPlots.toMutableList()

        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()

            if (next in allPlots) continue

            allPlots += next
            next.connectedPlots.filter { it !in queue }.forEach { queue += it }
        }

        allPlots
    }

    val totalPerimeter get() = allPlots.sumOf { it.perimeter }
    val totalArea get() = allPlots.size

    fun findNeighbors(plots: List<Day12Plot>) {
        connectedPlots.addAll(plots.filter {
            ((abs(it.x - x) == 1 && it.y - y == 0) || (it.x - x == 0 && abs(it.y - y) == 1)) && it.type == type
        })
    }

    fun generateUniqueId() {
        if (uniqueId != null) return

        uniqueId = nextId++

        allPlots.forEach { it.uniqueId = uniqueId }
    }

    companion object {
        var nextId = 0
    }
}