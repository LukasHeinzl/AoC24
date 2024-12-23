import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day19.txt").toFile().readText()
    val (availableLine, desiredLines) = input.split("\n\n")
    val available = availableLine.split(", ")
    val desired = desiredLines.split("\n")
    val memory = mutableMapOf<String, Long>()

    val partOne = desired.count { isPossible(it, available, memory) > 0 }
    println("Part one: $partOne")

    val partTwo = desired.sumOf { isPossible(it, available, memory) }
    println("Part two: $partTwo")
}

fun isPossible(
    desired: String,
    available: List<String>,
    memory: MutableMap<String, Long> = mutableMapOf()
): Long {
    if (desired.isEmpty()) return 1L
    if (desired in memory) return memory[desired]!!

    memory[desired] = available.sumOf {
        if (!desired.startsWith(it)) 0
        else {
            val result = isPossible(desired.substring(it.length), available, memory)
            memory[desired] = result
            result
        }
    }

    return memory[desired]!!
}