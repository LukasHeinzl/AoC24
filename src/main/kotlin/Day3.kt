import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day3.txt").toFile().readText()
    val matchesPartOne = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)").findAll(input)
    val partOneSum = matchesPartOne.sumOf {
        it.groupValues.drop(1)
            .map { v -> v.toInt() }
            .foldRight(1) { acc, v -> acc * v }
            .toInt()
    }

    println("Part one: $partOneSum")

    val matchesPartTwo = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)|do\\(\\)|don't\\(\\)").findAll(input)
    var partTwoSum = 0
    var multiplicationActive = true

    for (match in matchesPartTwo) {
        val instruction = match.groupValues[0].substringBefore('(')

        when (instruction) {
            "mul" -> if (multiplicationActive) partTwoSum += match.groupValues[1].toInt() * match.groupValues[2].toInt()
            "do" -> multiplicationActive = true
            "don't" -> multiplicationActive = false
        }
    }

    println("Part two: $partTwoSum")
}