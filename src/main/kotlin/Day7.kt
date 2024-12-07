import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day7.txt").toFile().readText()
    val equations = input.split("\n").map { it.toDay7Equation() }
    val partOneSum =
        equations.filter { it.isPossible(listOf(MathOperator.PLUS, MathOperator.MULTIPLY)) }.sumOf { it.result }
    println("Part one: $partOneSum")

    val partTwoSum =
        equations.filter { it.isPossible(listOf(MathOperator.PLUS, MathOperator.MULTIPLY, MathOperator.CONCATENATE)) }
            .sumOf { it.result }
    println("Part two: $partTwoSum")
}

fun String.toDay7Equation(): Day7Equation {
    val (result, inputsRaw) = this.split(": ")
    val inputs = inputsRaw.split(" ").map { it.trim().toLong() }
    return Day7Equation(result.toLong(), inputs)
}

enum class MathOperator(val calculate: (Long, Long) -> Long) {
    PLUS({ v1, v2 -> v1 + v2 }),
    MULTIPLY({ v1, v2 -> v1 * v2 }),
    CONCATENATE({ v1, v2 -> "$v1$v2".toLong() })
}

data class Day7Equation(val result: Long, val inputs: List<Long>) {
    fun isPossible(
        availableOperators: List<MathOperator>,
        index: Int = 0,
        foundOperators: List<MathOperator> = listOf()
    ): Boolean {
        if (index == inputs.size - 1) {
            return result == calculate(foundOperators)
        }

        for (operator in availableOperators) {
            val newOperatorList = foundOperators.toMutableList()
            newOperatorList.add(operator)

            if (isPossible(availableOperators, index + 1, newOperatorList)) {
                return true
            }
        }

        return false
    }

    private fun calculate(operators: List<MathOperator>): Long {
        var result = inputs[0]

        for (i in operators.indices) {
            result = operators[i].calculate(result, inputs[i + 1])
        }

        return result
    }
}