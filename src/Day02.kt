import Result.*
import Shape.*

fun main() {
    fun part1(input: List<String>): Int =
        input.asInputForPart1()
            .sumOf { (opponentsShape, yourShape) ->
                val result = yourShape.against(opponentsShape)
                yourShape.score() + result.score()
            }


    fun part2(input: List<String>): Int =
        input.asInputForPart2()
            .sumOf { (opponentsShape, result) ->
                val yourShape = when (result) {
                    WIN -> opponentsShape.losesAgainst()
                    LOSE -> opponentsShape.winsAgainst()
                    DRAW -> opponentsShape
                }

                yourShape.score() + result.score()
            }

    val input = readInput("Day02")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == 15) { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(exampleResult == 12) { "Part 2 result was $exampleResult" }

        println("Part 2: ${this(input)}")
    }
}

private enum class Shape(private val score: Int) {
    ROCK(1) {
        override fun losesAgainst() = PAPER
        override fun winsAgainst() = SCISSORS
    },
    PAPER(2) {
        override fun losesAgainst() = SCISSORS
        override fun winsAgainst() = ROCK
    },
    SCISSORS(3) {
        override fun losesAgainst() = ROCK
        override fun winsAgainst() = PAPER
    };

    abstract fun losesAgainst(): Shape
    abstract fun winsAgainst(): Shape

    fun against(other: Shape): Result = when (other) {
        winsAgainst() -> WIN
        losesAgainst() -> LOSE
        else -> DRAW
    }

    fun score(): Int = score
}

private enum class Result(private val score: Int) {
    WIN(6), LOSE(0), DRAW(3);

    fun score(): Int = score
}

private fun List<String>.asInputForPart1(): List<Pair<Shape, Shape>> = this
    .map {
        val opponent = when (it[0]) {
            'A' -> ROCK
            'B' -> PAPER
            'C' -> SCISSORS
            else -> throw IllegalArgumentException("Unknown Play ${it[0]}")
        }

        val you = when (it[2]) {
            'X' -> ROCK
            'Y' -> PAPER
            'Z' -> SCISSORS
            else -> throw IllegalArgumentException("Unknown Play ${it[2]}")
        }

        opponent to you
    }

private fun List<String>.asInputForPart2(): List<Pair<Shape, Result>> = this
    .map {
        val opponent = when (it[0]) {
            'A' -> ROCK
            'B' -> PAPER
            'C' -> SCISSORS
            else -> throw IllegalArgumentException("Unknown Play ${it[0]}")
        }

        val expectedResult = when (it[2]) {
            'X' -> LOSE
            'Y' -> DRAW
            'Z' -> WIN
            else -> throw IllegalArgumentException("Unknown Result ${it[2]}")
        }

        opponent to expectedResult
    }

private fun example() = """
        A Y
        B X
        C Z
    """.trimIndent().lines()