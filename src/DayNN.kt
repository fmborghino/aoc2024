fun main() {
    val day="NN"

    fun part1(input: List<String>): Int {
        return input.sumOf { it.toInt() }
    }

    fun part2(input: List<String>): Int {
        return 2 * input.sumOf { it.toInt() }
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 6)
    verify("Real part 1", part1(readInput("Day${day}")), 600)
    verify("Test part 2", part2(readInput("Day${day}_test")) , 12)
    verify("Real part 2", part2(readInput("Day${day}")), 1200)
}
