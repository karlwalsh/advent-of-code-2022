fun main() {
    fun part1(input: List<String>): String {
        val (stacks, instructions) = input.asStacksAndInstructions()

        instructions.forEach {
            stacks.handle(it) { from, to, quantity ->
                from.moveSingleCrateAtOnce(quantity, to)
            }
        }

        return stacks.message()
    }

    fun part2(input: List<String>): String {
        val (stacks, instructions) = input.asStacksAndInstructions()

        instructions.forEach {
            stacks.handle(it) { from, to, quantity ->
                from.moveAllCratesAtOnce(quantity, to)
            }
        }

        return stacks.message()
    }

    val input = readInput("Day05")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == "CMZ") { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(exampleResult == "MCD") { "Part 2 result was $exampleResult" }

        println("Part 2: ${this(input)}")
    }
}

private fun List<String>.asStacksAndInstructions(): Pair<Stacks, List<Instruction>> {
    val split = this.indexOf("") //First empty line splits crate stacks and instructions

    //Parse position numbers - get the max, so we can size the array of stacks
    val numberOfPositions = this[split - 1].split(" ")
        .filter { it.isNotBlank() }
        .maxOfOrNull { it.toInt() }
        ?: throw IllegalStateException("Cannot find number of positions")


    //Parse and populate stacks
    val stacks = Array(numberOfPositions) { CrateStack() }
    subList(0, split - 1).map { line ->
        line.forEachIndexed { index, c ->
            if (c in 'A'..'Z') {
                //Convert to zero-based stack index
                val position = if (index == 1) 0 else ((index - 2) / 4) + 1
                stacks[position].add(c)
            }
        }
    }

    //Parse move Instructions
    val instructionsRegex = """move (?<quantity>\d+) from (?<from>\d) to (?<to>\d)""".toRegex()
    val instructions = subList(split + 1, size).map {
        val groups = instructionsRegex.matchEntire(it)?.groups!!
        Instruction(groups["quantity"].toInt(), groups["from"].toInt(), groups["to"].toInt())
    }

    return Stacks(stacks) to instructions
}

private fun MatchGroup?.toInt() = this!!.value.toInt()

private class Stacks(private val stacks: Array<CrateStack>) {
    fun handle(instruction: Instruction, func: (CrateStack, CrateStack, Int) -> Unit) {
        val quantity = instruction.quantity
        val from = instruction.from - 1
        val to = instruction.to - 1

        func(stacks[from], stacks[to], quantity)
    }

    fun message(): String = stacks
        .mapNotNull { it.topCrate() }
        .joinToString(separator = "")
}

private class CrateStack {
    private val crates: MutableList<Char> = mutableListOf()

    fun add(crate: Char) {
        crates.add(crate)
    }

    fun moveSingleCrateAtOnce(quantity: Int, other: CrateStack) {
        repeat(quantity) { other.crates.add(0, crates.removeAt(0)) }
    }

    fun moveAllCratesAtOnce(quantity: Int, other: CrateStack) {
        repeat(quantity) { index -> other.crates.add(index, crates.removeAt(0)) }
    }

    fun topCrate(): Char? {
        return if (crates.isEmpty()) null else crates[0]
    }
}

private data class Instruction(val quantity: Int, val from: Int, val to: Int)

private fun example() = """
            [D]    
        [N] [C]    
        [Z] [M] [P]
         1   2   3 
        
        move 1 from 2 to 1
        move 3 from 1 to 3
        move 2 from 2 to 1
        move 1 from 1 to 2
    """.trimIndent().lines()