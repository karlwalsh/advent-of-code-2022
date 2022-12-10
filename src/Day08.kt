@file:OptIn(ExperimentalUnsignedTypes::class)

@ExperimentalUnsignedTypes
fun main() {
    fun part1(input: List<String>): Int {
        val map = input.toHeightMap()

        //Set of visible trees, starting with all the edge trees
        val visible: MutableSet<Point> = mutableSetOf()
        map.indices.map { y -> Point(y, 0) }.forEach(visible::add)
        map.indices.map { y -> Point(y, map[0].size - 1) }.forEach(visible::add)
        map[0].indices.map { x -> Point(0, x) }.forEach(visible::add)
        map[0].indices.map { x -> Point(map.size - 1, x) }.forEach(visible::add)


        //Horizontal iteration
        for (y in 1.until(map.size - 1)) {
            val edgeHeightFromLeft = map[y][0]
            var peak = edgeHeightFromLeft

            //Left to right
            for (x in 1.until(map[y].size - 1)) {
                val current = map[y][x]
                if (current > peak) {
                    visible.add(Point(y, x))
                    peak = current
                }
            }

            val edgeHeightFromRight = map[y][map[y].size - 1]
            peak = edgeHeightFromRight

            //Right to left
            for (x in (map[y].size - 1).downTo(1)) {
                val current = map[y][x]
                if (current > peak) {
                    visible.add(Point(y, x))
                    peak = current
                }
            }
        }

        //Vertical iteration
        for (x in 1.until(map[0].size - 1)) {
            val edgeHeightFromTop = map[0][x]
            var peak = edgeHeightFromTop

            //Top to bottom
            for (y in 1.until(map.size - 1)) {
                val current = map[y][x]
                if (current > peak) {
                    visible.add(Point(y, x))
                    peak = current
                }
            }

            val edgeHeightFromBottom = map[map.size - 1][x]
            peak = edgeHeightFromBottom

            //Bottom to top
            for (y in (map.size - 1).downTo(1)) {
                val current = map[y][x]
                if (current > peak) {
                    visible.add(Point(y, x))
                    peak = current
                }
            }
        }

        return visible.size
    }


    fun part2(input: List<String>): Int {
        val map = input.toHeightMap()

        var biggestScenicScore = 0

        //Iterate through all interior trees, and then search in four directions from each
        for (startY in 1.until(map.size - 1)) {
            for (startX in 1.until(map[startY].size - 1)) {

                val startHeight = map[startY][startX]

                val visibleToTheLeft = with(startX) {
                    var count = 0
                    for (x in (this - 1).downTo(0)) {
                        count++
                        if (map[startY][x] >= startHeight) break
                    }
                    count
                }

                val visibleToTheTop = with(startY) {
                    var count = 0
                    for (y in (this - 1).downTo(0)) {
                        count++
                        if (map[y][startX] >= startHeight) break
                    }
                    count
                }

                val visibleToTheRight = with(startX) {
                    var count = 0
                    for (x in (this + 1).until(map[startY].size)) {
                        count++
                        if (map[startY][x] >= startHeight) break
                    }
                    count

                }

                val visibleToTheBottom = with(startY) {
                    var count = 0
                    for (y in (this + 1).until(map.size)) {
                        count++
                        if (map[y][startX] >= startHeight) break
                    }
                    count
                }

                val scenicScore = visibleToTheLeft * visibleToTheTop * visibleToTheRight * visibleToTheBottom

                biggestScenicScore = biggestScenicScore.coerceAtLeast(scenicScore)
            }
        }

        return biggestScenicScore
    }

    val input = readInput("Day08")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == 21) { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(exampleResult == 8) { "Part 2 result was $exampleResult" }

        println("Part 2: ${this(input)}")
    }
}

private data class Point(val y: Int, val x: Int)

private fun List<String>.toHeightMap(): Array<UByteArray> {
    val width = this[0].length
    val height = this.size
    val map = Array(width) { UByteArray(height) { 0u } }

    this.forEachIndexed { y: Int, line ->
        line.map { it.digitToInt().toUByte() }.forEachIndexed { x, height ->
            map[y][x] = height
        }
    }

    return map
}

private fun example() = """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent().lines()