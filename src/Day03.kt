fun main() {
    fun priorityOf(char: Char): Int {
        val offset = when (char) {
            in 'a'..'z' -> 96
            in 'A'..'Z' -> 38
            else -> throw IllegalArgumentException("Unknown char $char")
        }
        return char.code - offset
    }

    fun part1(input: List<String>): Int = input.asInputForPart1().sumOf { priorityOf(it.common()) }
    fun part2(input: List<String>): Int = input.asInputForPart2().sumOf { priorityOf(it.intersection()) }

    val input = readInput("Day03")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == 157) { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(exampleResult == 70) { "Part 2 result was $exampleResult" }

        println("Part 2: ${this(input)}")
    }
}

private fun List<String>.asInputForPart1(): List<Rucksack> = this.map { contents ->
    val midPoint = contents.length / 2

    val firstCompartment = contents.substring(0 until midPoint).toSet()
    val secondCompartment = contents.substring(midPoint until contents.length).toSet()

    Rucksack(firstCompartment, secondCompartment)
}

private fun List<String>.asInputForPart2(): List<Group> = this.asInputForPart1()
    .windowed(3, 3, false)
    .map { Group(it[0], it[1], it[2]) }

private data class Rucksack(val firstCompartment: Set<Char>, val secondCompartment: Set<Char>) {
    fun common(): Char {
        val intersect = firstCompartment.intersect(secondCompartment)
        assert(intersect.size == 1) { "Assumed rucksack compartments have a single common item, but that didn't hold up: $intersect" }
        return intersect.first()
    }

    fun all(): Set<Char> = firstCompartment.union(secondCompartment)
}

private data class Group(val a: Rucksack, val b: Rucksack, val c: Rucksack) {
    fun intersection(): Char {
        val intersect = a.all().intersect(b.all()).intersect(c.all())
        assert(intersect.size == 1) { "Assumed rucksacks to have a single common item, but that didn't hold up: $intersect" }
        return intersect.first()
    }
}

private fun example() = """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent().lines()