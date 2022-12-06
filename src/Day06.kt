fun main() {
    fun part1(input: String): Int = input.windowedSequence(4)
        .mapIndexed { index, chunk -> index to chunk }
        .filter { (_, chunk) -> chunk.toSet().size == 4 }
        .first()
        .first + 4

    fun part2(input: String): Int = input.windowedSequence(14)
        .mapIndexed { index, chunk -> index to chunk }
        .filter { (_, chunk) -> chunk.toSet().size == 14 }
        .first()
        .first + 14

    val input = readInput("Day06").first()

    with(::part1) {
        mapOf(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 7,
            "bvwbjplbgvbhsrlpgdmjqwftvncz" to 5,
            "nppdvjthqldpwncqszvftbrmjlhg" to 6,
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 10,
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 11
        ).forEach { (example, expectedResult) ->
            val exampleResult = this(example)
            check(exampleResult == expectedResult) {
                "Part 1 result for '$example' was $exampleResult but expected $expectedResult: '${
                    example.substring(
                        0 until expectedResult
                    )
                }|${example.substring(expectedResult until example.length)}'"
            }
        }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        mapOf(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 19,
            "bvwbjplbgvbhsrlpgdmjqwftvncz" to 23,
            "nppdvjthqldpwncqszvftbrmjlhg" to 23,
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 29,
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 26
        ).forEach { (example, expectedResult) ->
            val exampleResult = this(example)
            check(exampleResult == expectedResult) {
                "Part 2 result for '$example' was $exampleResult but expected $expectedResult: '${
                    example.substring(
                        0 until expectedResult
                    )
                }|${example.substring(expectedResult until example.length)}'"
            }

        }

        println("Part 2: ${this(input)}")
    }
}