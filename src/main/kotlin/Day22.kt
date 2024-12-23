import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day22.txt").toFile().readText()
    val buyers = input.split("\n").map { it.toLong() }
    val partOne = buyers.sumOf {
        var result = it

        for (i in 1..2000) {
            result = result.nextSecretNumber()
        }

        result
    }

    println("Part one: $partOne")
}

fun Long.nextSecretNumber(): Long {
    var step1 = this * 64
    step1 = this xor step1
    step1 %= 16777216

    var step2 = step1 / 32
    step2 = step1 xor step2
    step2 %= 16777216

    var step3 = step2 * 2048
    step3 = step2 xor step3

    return step3 % 16777216
}