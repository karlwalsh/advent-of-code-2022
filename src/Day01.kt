fun main() {
    fun solve(input: List<Int>, n: Int): Int = input.sortedDescending().take(n).sum()
    fun part1(input: List<String>): Int = solve(input.toDaySpecificInput(), 1)
    fun part2(input: List<String>): Int = solve(input.toDaySpecificInput(), 3)

    val input = readInput("Day01")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == 24000) { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(exampleResult == 45000) { "Part 2 result was $exampleResult" }

        println("Part 2: ${this(input)}")
    }
}

private fun List<String>.toDaySpecificInput(): List<Int> {
    val puzzleInput: MutableList<Int> = mutableListOf()

    var currentSum = 0
    this.forEach { value ->
        if (value.isBlank()) {
            puzzleInput.add(currentSum)
            currentSum = 0
        } else {
            currentSum += value.toInt()
        }
    }

    if (currentSum > 0) puzzleInput.add(currentSum)

    return puzzleInput
}

private fun example() = """
        1000
        2000
        3000

        4000

        5000
        6000

        7000
        8000
        9000

        10000
    """.trimIndent().lines()