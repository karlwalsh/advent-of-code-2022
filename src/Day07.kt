fun main() {
    fun part1(input: List<String>): Long = input.toRootDirectory()
        .allDirectories()
        .map { it.size() }
        .filter { it <= 100000 }
        .sum()

    fun part2(input: List<String>): Long {
        val root = input.toRootDirectory()

        val totalDiskSize = 70_000_000
        val requiredDiskSpace = 30_000_000
        val availableDiskSpace = totalDiskSize - root.size()

        return root.allDirectories()
            .map { it.size() }
            .sorted()
            .first { availableDiskSpace + it >= requiredDiskSpace }
    }

    val input = readInput("Day07")

    with(::part1) {
        val exampleResult = this(example())
        check(exampleResult == 95437L) { "Part 1 result was $exampleResult" }

        println("Part 1: ${this(input)}")
    }

    with(::part2) {
        val exampleResult = this(example())
        check(exampleResult == 24933642L) { "Part 2 result was $exampleResult" }

        println("Part 2: ${this(input)}")
    }
}

private class Directory(private val parent: Directory? = null) {

    private val directories: MutableList<Directory> = mutableListOf()
    private var filesize: Long = 0

    fun parent(): Directory? = parent

    fun newDirectory(): Directory {
        val newDirectory = Directory(this)
        directories.add(newDirectory)
        return newDirectory
    }

    fun newFile(filesize: Long) {
        this.filesize += filesize
    }

    fun allDirectories(): List<Directory> {
        return directories + directories.flatMap { it.allDirectories() }
    }

    fun size(): Long = filesize + directories.sumOf { it.size() }
}

private fun List<String>.toRootDirectory(): Directory {
    //Always start with a root directory
    val root = Directory()

    //current working directory
    var cwd = root

    val fileParser = FileParser()

    for (line in this) {
        when {
            // Ignore cd into root, we already have a root directory
            line.startsWith("$ cd /") -> continue

            // Ignore the ls command because we can pick up directory and file listings without it
            line.startsWith("$ ls") -> continue

            //Set cwd to parent
            line.startsWith("$ cd ..") -> cwd = cwd.parent() ?: cwd

            //Add directory to the cwd, and set cwd to the new directory
            line.startsWith("$ cd ") -> cwd = cwd.newDirectory()

            //Directory listing - Add directory to cwd
            line.startsWith("dir ") -> cwd.newDirectory()

            //File listing - Add file to cwd
            fileParser.matches(line) -> cwd.newFile(fileParser.filesize(line))
        }
    }

    return root
}

private class FileParser() {
    companion object {
        val filenameRegex = """(?<size>\d+).+""".toRegex()
    }

    fun matches(listing: String): Boolean = filenameRegex.matches(listing)

    fun filesize(listing: String): Long {
        val match =
            filenameRegex.matchEntire(listing) ?: throw IllegalArgumentException("Not a file listing '$listing'")

        return match.groups["size"]!!.value.toLong()
    }
}

private fun example() = """
        ${'$'} cd /
        ${'$'} ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        ${'$'} cd a
        ${'$'} ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        ${'$'} cd e
        ${'$'} ls
        584 i
        ${'$'} cd ..
        ${'$'} cd ..
        ${'$'} cd d
        ${'$'} ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    """.trimIndent().lines()