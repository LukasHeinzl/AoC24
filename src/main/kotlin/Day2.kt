import java.nio.file.Paths
import kotlin.math.abs

fun main() {
    val input = Paths.get("src/main/resources/day2.txt").toFile().readText()
    val lines = input.split("\n")
    val reports = lines.map { Day2Report(it) }
    val safeReportsPartOne = reports.count { it.isSafe() }
    println("Part one: $safeReportsPartOne")

    val safeReportsPartTwo = reports.count { it.isSafe(true) }
    println("Part two: $safeReportsPartTwo")
}

class Day2Report(private var levels: List<Int>) {
    constructor(input: String) : this(input.split(" ").map { it.toInt() })

    fun isSafe(partTwo: Boolean = false): Boolean {
        val differences = mutableListOf<Int>()

        for (i in levels.indices.drop(1)) {
            differences.add(levels[i - 1] - levels[i])
        }

        if (differences.any { abs(it) !in 1..3 }) {
            if (partTwo) {
                for (i in levels.indices) {
                    if (checkOneRemovedSafe(i)) {
                        return true
                    }
                }
            }

            return false
        }

        val increasing = differences[0] > 0

        if (differences.any { if (increasing) it < 0 else it > 0 }) {
            if (partTwo) {
                for (i in levels.indices) {
                    if (checkOneRemovedSafe(i)) {
                        return true
                    }
                }
            }

            return false
        }

        return true
    }

    private fun checkOneRemovedSafe(index: Int): Boolean {
        val removed = levels.toMutableList()
        removed.removeAt(index)

        return Day2Report(removed).isSafe()
    }
}