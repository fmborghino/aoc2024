fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { it.toInt() }
    }

    fun part2(input: List<String>): Int {
        return 2 * input.sumOf { it.toInt() }
    }

    verify("Test part 1", part1(readInput("DayNN_test")), 6)

    verify("Puzzle part 1", part1(readInput("DayNN")), 600)

    verify("Test part 2", part2(readInput("DayNN_test")) , 12)

    verify("Puzzle part 2", part2(readInput("DayNN")), 1200)
}
