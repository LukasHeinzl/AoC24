import java.nio.file.Paths
import kotlin.math.abs

fun main() {
    val input = Paths.get("src/main/resources/day1.txt").toFile().readText()
    val lines = input.split("\n")
    val pairs = lines.map { it.splitIntoNumberPairs() }
    val leftSideSorted = pairs.map { it.first }.sorted()
    val rightSideSorted = pairs.map { it.second }.sorted()

    val sumOfDifferences = leftSideSorted.mapIndexed { idx, left -> abs(left - rightSideSorted[idx]) }.sum()
    println("Part one: $sumOfDifferences")

    val similarityScore = leftSideSorted.sumOf { left ->
        left * rightSideSorted.count { right -> right == left }
    }
    println("Part two: $similarityScore")
}

fun String.splitIntoNumberPairs(): Pair<Int, Int> {
    val (first, second) = this.split("   ")
    return Pair(first.toInt(), second.toInt())
}