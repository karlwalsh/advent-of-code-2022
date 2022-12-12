import CpuInstruction.AddX
import CpuInstruction.Noop

fun main() {
    fun part1(input: List<String>): Int {
        val cyclesToCheck = setOf(20, 60, 100, 140, 180, 220)

        var x = 1
        var cycle = 1
        var signalStrength = 0

        fun sampleSignalStrength() {
            if (cycle in cyclesToCheck) signalStrength += (cycle * x)
        }

        input.toCpuInstructions().forEach { instruction ->
            when (instruction) {
                is Noop -> {
                    cycle++
                    sampleSignalStrength()
                }

                is AddX -> {
                    cycle++
                    sampleSignalStrength()

                    cycle++
                    x += instruction.value
                    sampleSignalStrength()
                }
            }
        }

        return signalStrength
    }

    fun part2(input: List<String>): String {
        var x = 1
        var cycle = 1

        var cursor = 0
        val display = StringBuilder()

        fun draw() {
            if (cursor in (x - 1)..(x + 1)) {
                display.append('#')
            } else {
                display.append('.')
            }
            if (cycle % 40 == 0) {
                display.append('\n')
                cursor = 0
            } else {
                cursor++
            }
        }

        input.toCpuInstructions().forEach { instruction ->
            draw()
            when (instruction) {
                is Noop -> {
                    cycle++
                }

                is AddX -> {
                    cycle++

                    draw()
                    x += instruction.value
                    cycle++
                }
            }
        }

        return display.toString().trim()
    }

    val input = readInput("Day10")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == 13140) { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(
            exampleResult == """
                ##..##..##..##..##..##..##..##..##..##..
                ###...###...###...###...###...###...###.
                ####....####....####....####....####....
                #####.....#####.....#####.....#####.....
                ######......######......######......####
                #######.......#######.......#######.....
            """.trimIndent()
        ) { "Part 2 result was\n$exampleResult" }

        println("Part 2:\n${this(input)}")
    }
}

private sealed class CpuInstruction {
    object Noop : CpuInstruction()
    data class AddX(val value: Int) : CpuInstruction()
}

private fun List<String>.toCpuInstructions(): List<CpuInstruction> = this.map {
    when {
        it == "noop" -> Noop
        it.startsWith("addx") -> AddX(it.substringAfter("addx ").toInt())
        else -> throw IllegalArgumentException("Unknown instruction $it")
    }
}

private fun example() = """
        addx 15
        addx -11
        addx 6
        addx -3
        addx 5
        addx -1
        addx -8
        addx 13
        addx 4
        noop
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx -35
        addx 1
        addx 24
        addx -19
        addx 1
        addx 16
        addx -11
        noop
        noop
        addx 21
        addx -15
        noop
        noop
        addx -3
        addx 9
        addx 1
        addx -3
        addx 8
        addx 1
        addx 5
        noop
        noop
        noop
        noop
        noop
        addx -36
        noop
        addx 1
        addx 7
        noop
        noop
        noop
        addx 2
        addx 6
        noop
        noop
        noop
        noop
        noop
        addx 1
        noop
        noop
        addx 7
        addx 1
        noop
        addx -13
        addx 13
        addx 7
        noop
        addx 1
        addx -33
        noop
        noop
        noop
        addx 2
        noop
        noop
        noop
        addx 8
        noop
        addx -1
        addx 2
        addx 1
        noop
        addx 17
        addx -9
        addx 1
        addx 1
        addx -3
        addx 11
        noop
        noop
        addx 1
        noop
        addx 1
        noop
        noop
        addx -13
        addx -19
        addx 1
        addx 3
        addx 26
        addx -30
        addx 12
        addx -1
        addx 3
        addx 1
        noop
        noop
        noop
        addx -9
        addx 18
        addx 1
        addx 2
        noop
        noop
        addx 9
        noop
        noop
        noop
        addx -1
        addx 2
        addx -37
        addx 1
        addx 3
        noop
        addx 15
        addx -21
        addx 22
        addx -6
        addx 1
        noop
        addx 2
        addx 1
        noop
        addx -10
        noop
        noop
        addx 20
        addx 1
        addx 2
        addx 2
        addx -6
        addx -11
        noop
        noop
        noop
    """.trimIndent().lines()