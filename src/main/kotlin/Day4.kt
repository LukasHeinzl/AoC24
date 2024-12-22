import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day4.txt").toFile().readText()
    val grid = input.split("\n")
    val searchCanvas = "XMAS".generateRotations()
    val partOneFound = findAppearances(searchCanvas, grid)
    println("Part one: $partOneFound")

    val searchCanvas2 = listOf(
        listOf("M.S", ".A.", "M.S"),
        listOf("M.M", ".A.", "S.S"),
        listOf("S.M", ".A.", "S.M"),
        listOf("S.S", ".A.", "M.M")
    )
    val partTwoFound = findAppearances(searchCanvas2, grid)
    println("Part two: $partTwoFound")
}

fun findAppearances(searchCanvas: List<List<String>>, grid: List<String>): Int {
    var found = 0

    for (search in searchCanvas) {
        for (y in 0..grid.size - search.size) {
            col@ for (x in 0..grid[y].length - search[0].length) {
                for (searchY in search.indices) {
                    for (searchX in search[searchY].indices) {
                        if (search[searchY][searchX] == '.') {
                            continue
                        }

                        if (search[searchY][searchX] != grid[y + searchY][x + searchX]) {
                            continue@col
                        }
                    }
                }

                found++
            }
        }
    }

    return found
}

fun String.generateRotations(): List<List<String>> {
    val rotations = mutableListOf(
        listOf(this), // Row
        listOf(this.reversed()), // Row reversed
        this.toList().map { it + "" }, // Column
        this.reversed().toList().map { it + "" }) // Column reversed
    var tmp = mutableListOf<String>()

    // Diagonal down right
    for (i in indices) {
        tmp += (this[i] + "").padStart(i + 1, '.').padEnd(length, '.')
    }

    rotations += tmp
    tmp = mutableListOf()

    // Diagonal down left
    for (i in indices) {
        tmp += (this[i] + "").padEnd(i + 1, '.').padStart(length, '.')
    }

    rotations += tmp
    tmp = mutableListOf()

    // Diagonal top left
    for (i in indices) {
        tmp += (reversed()[i] + "").padStart(i + 1, '.').padEnd(length, '.')
    }

    rotations += tmp
    tmp = mutableListOf()

    // Diagonal top right
    for (i in indices) {
        tmp += (reversed()[i] + "").padEnd(i + 1, '.').padStart(length, '.')
    }

    rotations += tmp

    return rotations
}