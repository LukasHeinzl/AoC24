import java.nio.file.Paths

fun main() {
    val input = Paths.get("src/main/resources/day17.txt").toFile().readText()
    val input2 = "Register A: 2024\n" +
            "Register B: 0\n" +
            "Register C: 0\n" +
            "\n" +
            "Program: 0,3,5,4,3,0"
    val program = input.toDay17Program()
    program.run()

    println("Part one: ${program.output.joinToString(",")}")
}

fun String.toDay17Program(): Day17Program {
    val (regA, regB, regC, _, prog) = split("\n")
    val regex = Regex("([0-9]+)")

    val a = regex.find(regA)!!.groupValues[1].toLong()
    val b = regex.find(regB)!!.groupValues[1].toLong()
    val c = regex.find(regC)!!.groupValues[1].toLong()
    val data = prog.substringAfter(" ").split(",").map { it.toLong() }

    return Day17Program(a, b, c, 0, data)
}

data class Day17Program(
    var registerA: Long,
    var registerB: Long,
    var registerC: Long,
    var ip: Int,
    val data: List<Long>,
    val output: MutableList<Long> = mutableListOf()
) {
    val nextLiteralOperand get() = if (ip + 1 !in data.indices) null else data[ip + 1]
    val nextComboOperand
        get() = if (ip + 1 !in data.indices) null else when (data[ip + 1]) {
            0L -> 0L
            1L -> 1L
            2L -> 2L
            3L -> 3L
            4L -> registerA
            5L -> registerB
            6L -> registerC
            else -> null
        }
    private val nextInstruction
        get() = if (ip !in data.indices) null else when (data[ip]) {
            0L -> AdvInstruction
            1L -> BxlInstruction
            2L -> BstInstruction
            3L -> JnzInstruction
            4L -> BxcInstruction
            5L -> OutInstruction
            6L -> BdvInstruction
            7L -> CdvInstruction
            else -> null
        }

    fun run() {
        while (true) {
            if (nextInstruction == null) return
            if (!nextInstruction!!.perform(this)) return
            ip++
        }
    }
}

abstract class Day17Instruction {
    abstract fun perform(program: Day17Program): Boolean
}

object AdvInstruction : Day17Instruction() {
    override fun perform(program: Day17Program): Boolean {
        val operand = program.nextComboOperand ?: return false
        program.registerA = program.registerA shr operand.toInt()

        program.ip++
        return true
    }
}

object BxlInstruction : Day17Instruction() {
    override fun perform(program: Day17Program): Boolean {
        val operand = program.nextLiteralOperand ?: return false
        program.registerB = program.registerB xor operand

        program.ip++
        return true
    }
}

object BstInstruction : Day17Instruction() {
    override fun perform(program: Day17Program): Boolean {
        val operand = program.nextComboOperand ?: return false
        program.registerB = operand % 8

        program.ip++
        return true
    }
}

object JnzInstruction : Day17Instruction() {
    override fun perform(program: Day17Program): Boolean {
        if (program.registerA == 0L) return true

        val operand = program.nextLiteralOperand ?: return false

        program.ip = (operand - 1L).toInt()
        return true
    }
}

object BxcInstruction : Day17Instruction() {
    override fun perform(program: Day17Program): Boolean {
        program.registerB = program.registerB xor program.registerC

        program.ip++
        return true
    }
}

object OutInstruction : Day17Instruction() {
    override fun perform(program: Day17Program): Boolean {
        val operand = (program.nextComboOperand ?: return false) % 8
        program.output += operand

        program.ip++
        return true
    }
}

object BdvInstruction : Day17Instruction() {
    override fun perform(program: Day17Program): Boolean {
        val operand = program.nextComboOperand ?: return false
        program.registerB = program.registerA shr operand.toInt()

        program.ip++
        return true
    }
}

object CdvInstruction : Day17Instruction() {
    override fun perform(program: Day17Program): Boolean {
        val operand = program.nextComboOperand ?: return false
        program.registerC = program.registerA shr operand.toInt()

        program.ip++
        return true
    }
}