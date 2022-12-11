@file:OptIn(ExperimentalUnsignedTypes::class)

import Direction.*

@ExperimentalUnsignedTypes
fun main() {
    fun part1(input: List<String>): Int {
        val motions = input.toMotions()

        var head = Position(0, 0)
        var tail = head

        //Unique positions, containing the starting position
        val uniquePositions: MutableSet<Position> = mutableSetOf(tail)

        motions.forEach { (direction, steps) ->
            repeat(steps) {
                head = when (direction) {
                    UP -> head.copy(y = head.y + 1)
                    DOWN -> head.copy(y = head.y - 1)
                    LEFT -> head.copy(x = head.x - 1)
                    RIGHT -> head.copy(x = head.x + 1)
                }

                if (tail.isNotAdjacentTo(head)) {
                    while (tail.isNotAdjacentTo(head)) {
                        tail = tail.oneStepTowards(head)
                    }
                    uniquePositions.add(tail)
                }
            }
        }

        return uniquePositions.size
    }

    fun part2(input: List<String>): Int {
        val motions = input.toMotions()

        //Unique positions, containing the starting position
        val uniquePositions: MutableSet<Position> = mutableSetOf(Position(0, 0))

        //0 is the head, 9 is the tail
        val rope = Array(10) { Position(0, 0) }

        motions.forEach { (direction, steps) ->
            repeat(steps) {
                rope[0] = when (direction) {
                    UP -> rope[0].copy(y = rope[0].y + 1)
                    DOWN -> rope[0].copy(y = rope[0].y - 1)
                    LEFT -> rope[0].copy(x = rope[0].x - 1)
                    RIGHT -> rope[0].copy(x = rope[0].x + 1)
                }

                //Only consider moving knots if head and first knot are not adjacent
                if (rope[1].isNotAdjacentTo(rope[0])) {
                    1.until(10).forEach { knot ->
                        while (rope[knot].isNotAdjacentTo(rope[knot - 1])) {
                            rope[knot] = rope[knot].oneStepTowards(rope[knot - 1])
                        }
                    }
                }

                uniquePositions.add(rope[9])
            }
        }

        return uniquePositions.size
    }

    val input = readInput("Day09")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == 13) { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(exampleResult == 1) { "Part 2 result was $exampleResult" }

        println("Part 2: ${this(input)}")
    }
}

private fun List<String>.toMotions(): List<Motion> = this
    .map { Motion(Direction.parse(it.substringBefore(' ')), it.substringAfter(' ').toInt()) }

private data class Position(val y: Int, val x: Int) {
    fun isNotAdjacentTo(other: Position): Boolean = !isAdjacentTo(other)

    fun isAdjacentTo(other: Position): Boolean =
        (this.x == other.x || this.x == other.x - 1 || this.x == other.x + 1) &&
                (this.y == other.y || this.y == other.y - 1 || this.y == other.y + 1)

    fun oneStepTowards(other: Position): Position {
        val nextY = if (this.y < other.y) this.y + 1 else if (this.y > other.y) this.y - 1 else this.y
        val nextX = if (this.x < other.x) this.x + 1 else if (this.x > other.x) this.x - 1 else this.x

        return Position(nextY, nextX)
    }
}

private data class Motion(val direction: Direction, val steps: Int)
private enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    companion object {
        fun parse(direction: String): Direction = when (direction) {
            "U" -> UP
            "D" -> DOWN
            "L" -> LEFT
            "R" -> RIGHT
            else -> throw IllegalArgumentException("Unknown direction $direction")
        }
    }
}

private fun example() = """
        R 4
        U 4
        L 3
        D 1
        R 4
        D 1
        L 5
        R 2
    """.trimIndent().lines()