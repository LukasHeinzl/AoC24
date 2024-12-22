import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day14.txt").toFile().readText()
    val robots = input.split("\n").map { it.toDay14Robot() }
    val width = 101
    val height = 103
    val positionsAfter100Seconds = robots.map { it.positionAfterSeconds(100, width, height) }
    val robotsTopLeft = positionsAfter100Seconds.count { it.first in 0..<width / 2 && it.second in 0..<height / 2 }
    val robotsTopRight =
        positionsAfter100Seconds.count { it.first in (width / 2) + 1..<width && it.second in 0..<height / 2 }
    val robotsBottomLeft =
        positionsAfter100Seconds.count { it.first in 0..<width / 2 && it.second in (height / 2) + 1..<height }
    val robotsBottomRight =
        positionsAfter100Seconds.count { it.first in (width / 2) + 1..<width && it.second in (height / 2) + 1..<height }
    val partOne = robotsTopLeft * robotsTopRight * robotsBottomLeft * robotsBottomRight

    println("Part one: $partOne")

    var seconds = 1

    while (true) {
        val robotsAfterNSeconds = robots.map { it.positionAfterSeconds(seconds, width, height) }
        var result = ""

        for (y in 0..<height) {
            for (x in 0..<width) {
                result += if (Pair(x, y) in robotsAfterNSeconds) {
                    "X"
                } else {
                    "."
                }
            }
            result += "\n"
        }

        if ("XXXXXXXXXX" in result) {
            println("Part two: $seconds")
            break
        }

        seconds++
    }
}

fun String.toDay14Robot(): Day14Robot {
    val (_, x, y, vx, vy) = Regex("p=([-0-9]+),([-0-9]+) v=([-0-9]+),([-0-9]+)").find(this)!!.groupValues

    return Day14Robot(Pair(x.toInt(), y.toInt()), Pair(vx.toInt(), vy.toInt()))
}

operator fun Pair<Int, Int>.times(scalar: Int): Pair<Int, Int> = Pair(first * scalar, second * scalar)

data class Day14Robot(val position: Pair<Int, Int>, val velocity: Pair<Int, Int>) {
    fun positionAfterSeconds(seconds: Int, width: Int, height: Int): Pair<Int, Int> {
        var (newX, newY) = position + velocity * seconds

        while (newX < 0) newX += width
        while (newY < 0) newY += height

        return Pair(newX % width, newY % height)
    }
}