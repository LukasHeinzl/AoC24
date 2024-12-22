import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day9.txt").toFile().readText()

    val expanded = input.toBlocks()
    val partOneChecksum =
        expanded.expandToIndividualBlocks().compact().sumOf { block -> block.position * (block.id?.toLong() ?: 0) }
    println("Part one: $partOneChecksum")

    val partTwoChecksum = expanded.compact().filter { it.id != null }
        .sumOf { block -> (0 until block.length).sumOf { (block.position + it) * block.id!!.toLong() } }
    println("Part two: $partTwoChecksum")
}

fun String.toBlocks(): List<Day9Block> {
    var position = 0
    return mapIndexed { index, c ->
        val block = Day9Block(position, c - '0', if (index % 2 == 0) index / 2 else null)
        position += block.length
        block
    }
}

fun List<Day9Block>.expandToIndividualBlocks(): List<Day9Block> {
    var position = 0
    return map { block -> (0 until block.length).map { Day9Block(position++, 1, block.id) } }.flatten()
}

fun List<Day9Block>.compact(): List<Day9Block> {
    val files = filter { it.id != null }.reversed().toMutableList()
    val freeBlocks = filter { it.id == null }.toMutableList()

    for (fileIndex in files.indices) {
        val file = files[fileIndex]

        for (freeIndex in freeBlocks.indices) {
            val freeBlock = freeBlocks[freeIndex]

            if (freeBlock.position >= file.position) break
            if (freeBlock.length < file.length) continue

            files[fileIndex] = Day9Block(freeBlock.position, file.length, file.id)
            freeBlocks[freeIndex] = Day9Block(freeBlock.position + file.length, freeBlock.length - file.length, null)
            break
        }
    }

    return listOf(files, freeBlocks).flatten()
}

data class Day9Block(var position: Int, val length: Int, val id: Int?)