import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day6.txt").toFile().readText()
    val patrolPath = input.toPatrolPath()
    val path = patrolPath.getPath()
    val distinctPositions = path.first.size
    println("Part one: $distinctPositions")

    val loops = path.first.drop(1).count { patrolPath.getPath(it).second }
    println("Part two: $loops")
}

fun String.toPatrolPath(): PatrolPath {
    val lines = this.split("\n")
    val obstacles = mutableListOf<Pair<Int, Int>>()
    var startPosition: Pair<Int, Int>? = null

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, symbol ->
            if (symbol == '#') {
                obstacles += Pair(x, y)
            } else if (symbol == '^') {
                startPosition = Pair(x, y)
            }
        }
    }

    return PatrolPath(startPosition!!, obstacles, lines[0].length, lines.size)
}

enum class Direction(val delta: Pair<Int, Int>) {
    UP(Pair(0, -1)),
    RIGHT(Pair(1, 0)),
    DOWN(Pair(0, 1)),
    LEFT(Pair(-1, 0));

    fun nextDirection() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(this.first + other.first, this.second + other.second)

class PatrolPath(
    private val startPosition: Pair<Int, Int>,
    private val obstacles: List<Pair<Int, Int>>,
    private val maxX: Int,
    private val maxY: Int
) {
    fun getPath(extraBlock: Pair<Int, Int>? = null): Pair<List<Pair<Int, Int>>, Boolean> {
        val path = mutableMapOf<Pair<Int, Int>, MutableList<Direction>>()
        var currentDirection = Direction.UP
        var currentPosition = startPosition
        var foundLoop = false

        while (true) {
            if (currentPosition.first !in 0 until maxX || currentPosition.second !in 0 until maxY) {
                break
            }

            if (currentPosition in path) {
                if (currentDirection in path[currentPosition]!!) {
                    foundLoop = true
                    break
                }
            } else {
                path[currentPosition] = mutableListOf()
            }

            path[currentPosition]!! += currentDirection

            val nextPosition = currentPosition + currentDirection.delta

            if (nextPosition in obstacles || nextPosition == extraBlock) {
                currentDirection = currentDirection.nextDirection()
            } else {
                currentPosition = nextPosition
            }
        }

        return Pair(path.keys.toList(), foundLoop)
    }
}