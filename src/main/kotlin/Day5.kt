import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day5.txt").toFile().readText()
    val encodedInput = input.toDay5Input()
    val partOneSum = encodedInput.sumValidMiddlePages()
    println("Part one: $partOneSum")

    val partTwoSum = encodedInput.sumInvalidMiddlePages()
    println("Part two: $partTwoSum")
}

fun String.toDay5Input(): Day5Input {
    val (rules, updates) = this.split("\n\n")

    val rulesList = rules.split("\n").map { line ->
        val (firstPage, secondPage) = line.split("|")
        Day5Rule(firstPage.toInt(), secondPage.toInt())
    }

    val updatesList = updates.split("\n").map { line ->
        Day5Update(line.split(",").map { it.toInt() })
    }

    return Day5Input(rulesList, updatesList)
}

data class Day5Rule(val firstPage: Int, val secondPage: Int) {
    fun getSortOrder(update: Day5Update): Int {
        val firstPageIndex = update.pages.indexOf(firstPage)
        val secondPageIndex = update.pages.indexOf(secondPage)

        if (firstPageIndex == -1 || secondPageIndex == -1) return -1

        return firstPageIndex - secondPageIndex
    }
}

data class Day5Update(val pages: List<Int>)

data class Day5Input(val rules: List<Day5Rule>, val updates: List<Day5Update>) {
    fun sumValidMiddlePages() = updates.filter { update ->
        rules.all { rule -> rule.getSortOrder(update) < 0 }
    }.sumOf {
        it.pages[it.pages.size / 2]
    }

    fun sumInvalidMiddlePages() = updates.filter { update ->
        rules.any { rule -> rule.getSortOrder(update) >= 0 }
    }.sumOf { update ->
        val sortedPages = update.pages.sortedWith { v1, v2 ->
            val potentialRule =
                rules.find { rule -> (rule.firstPage == v1 && rule.secondPage == v2) || (rule.firstPage == v2 && rule.secondPage == v1) }

            return@sortedWith potentialRule?.getSortOrder(update) ?: 0
        }

        sortedPages[sortedPages.size / 2]
    }
}