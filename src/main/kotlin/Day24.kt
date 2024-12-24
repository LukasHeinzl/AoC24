import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day24.txt").toFile().readText()
    val (inputs, logic) = input.split("\n\n")
    val (wires, rawGates) = logic.extractLogic()
    val allWires = (inputs.extractWiresFromInputs() + wires).distinctBy { it.name }
    val gates = rawGates.map { it.toGate(allWires) }

    gates.forEach { it.tick() }
    val partOne = allWires.filter { it.name.startsWith('z') }.sumOf { it.toBit() }
    println("Part one: $partOne")
}

fun String.extractWiresFromInputs() = split("\n").map {
    val (name, state) = it.split(": ")
    Wire(name, state.toWireState())
}

fun String.extractLogic(): Pair<List<Wire>, List<RawGate>> {
    val wires = mutableListOf<Wire>()
    val gates = mutableListOf<RawGate>()

    split("\n").map {
        val (wire1, operation, wire2, _, wire3) = it.split(" ")

        wires += Wire(wire1)
        wires += Wire(wire2)
        wires += Wire(wire3)
        gates += RawGate(operation.toOperation()!!, wire1, wire2, wire3)
    }

    return Pair(wires, gates)
}

fun String.toWireState() = when (this) {
    "0" -> WireState.LOW
    "1" -> WireState.HIGH
    else -> WireState.UNDEFINED
}

fun String.toOperation() = when (this) {
    "AND" -> GateOperation.AND
    "OR" -> GateOperation.OR
    "XOR" -> GateOperation.XOR
    else -> null
}

enum class WireState {
    HIGH,
    LOW,
    UNDEFINED;

    infix fun and(other: WireState) = this == HIGH && other == HIGH
    infix fun or(other: WireState) = this == HIGH || other == HIGH
    infix fun xor(other: WireState) = (this == HIGH && other == LOW) || (this == LOW && other == HIGH)
}

enum class GateOperation(val check: (wire1: Wire, wire2: Wire) -> Boolean) {
    AND({ w1, w2 -> w1.state and w2.state }),
    OR({ w1, w2 -> w1.state or w2.state }),
    XOR({ w1, w2 -> w1.state xor w2.state })
}

data class Wire(
    val name: String,
    var state: WireState = WireState.UNDEFINED,
    val affectedGates: MutableList<Gate> = mutableListOf()
) {
    fun tick() = affectedGates.forEach { it.tick() }
    fun toBit() = (if (state == WireState.HIGH) 1L else 0L) shl name.substring(1).toInt()

    override fun toString() = "$name ($state)"
}

data class RawGate(val operation: GateOperation, val input1: String, val input2: String, val output: String) {
    fun toGate(wires: List<Wire>): Gate {
        val gate = Gate(
            operation,
            wires.find { it.name == input1 }!!,
            wires.find { it.name == input2 }!!,
            wires.find { it.name == output }!!
        )

        wires.find { it.name == input1 }!!.affectedGates += gate
        wires.find { it.name == input2 }!!.affectedGates += gate

        return gate
    }
}

data class Gate(val operation: GateOperation, val inputWire1: Wire, val inputWire2: Wire, val outputWire: Wire) {
    fun tick() {
        if (inputWire1.state == WireState.UNDEFINED || inputWire2.state == WireState.UNDEFINED) return

        outputWire.state = if (operation.check(inputWire1, inputWire2)) WireState.HIGH else WireState.LOW
        outputWire.tick()
    }
}