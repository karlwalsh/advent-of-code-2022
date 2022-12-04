fun main() {
    fun part1(input: List<String>): Int = input.asInputForPart1AndPart2().count { it.assignmentsFullyContained() }
    fun part2(input: List<String>): Int = input.asInputForPart1AndPart2().count { it.assignmentsOverlap() }

    val input = readInput("Day04")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == 2) { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(exampleResult == 4) { "Part 2 result was $exampleResult" }

        println("Part 2: ${this(input)}")
    }
}

private data class Pair(val first: Assignment, val second: Assignment) {
    fun assignmentsFullyContained(): Boolean = first.fullyContains(second) || second.fullyContains(first)
    fun assignmentsOverlap(): Boolean = first.overlaps(second) || second.overlaps(first)
}

private data class Assignment(val start: Int, val end: Int) {
    fun fullyContains(other: Assignment): Boolean = this.start <= other.start && this.end >= other.end
    fun overlaps(other: Assignment): Boolean = other.start in start..end || start in other.start..other.end
}

private fun List<String>.asInputForPart1AndPart2(): List<Pair> = this.map { it.toPair() }
private fun String.toPair() = Pair(this.split(",")[0].toAssignment(), this.split(",")[1].toAssignment())
private fun String.toAssignment() = Assignment(this.split("-")[0].toInt(), this.split("-")[1].toInt())

private fun example() = """
        2-4,6-8
        2-3,4-5
        5-7,7-9
        2-8,3-7
        6-6,4-6
        2-6,4-8
    """.trimIndent().lines()