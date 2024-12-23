import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day23.txt").toFile().readText()
    val connections = input.split("\n").map { it.split("-") }
    val computers = connections.map { connection -> connection.map { Day23Computer(it) } }.flatten()
    computers.forEach { it.loadConnections(connections, computers) }

    val partOne = computers.map { it.getLanParties() }.flatten().distinct()
        .filter { it.any { computer -> computer.name.startsWith("t") } }.size
    println("Part one: $partOne")

    var largestConnectedGroup = listOf<Day23Computer>()

    for (computer in computers) {
        val connectedComputers = mutableListOf(computer)

        for (connection in computer.connections) {
            if (connection.connections.containsAll(connectedComputers)) {
                connectedComputers += connection
            }
        }

        if (connectedComputers.size > largestConnectedGroup.size) {
            largestConnectedGroup = connectedComputers
        }
    }

    val partTwo = largestConnectedGroup.sortedBy { it.name }.joinToString(",")
    println("Part two: $partTwo")
}

data class Day23Computer(val name: String, val connections: MutableList<Day23Computer> = mutableListOf()) {
    fun loadConnections(connections: List<List<String>>, computers: List<Day23Computer>) {
        val relevantConnections = connections.filter { name in it }
        this.connections.addAll(computers.filter { computer ->
            computer.name != this.name && relevantConnections.any { computer.name in it }
        }.distinctBy { it.name })
    }

    fun getLanParties(): List<List<Day23Computer>> {
        val parties = mutableListOf<List<Day23Computer>>()

        for (c1 in connections) {
            for (c2 in c1.connections) {
                if (this in c2.connections) {
                    parties += listOf(this, c1, c2).sortedBy { it.name }
                }
            }
        }

        return parties.distinct()
    }

    override fun equals(other: Any?) = if (other is Day23Computer) other.name == name else false
    override fun hashCode() = name.hashCode()
    override fun toString() = name
}