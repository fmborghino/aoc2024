fun main() {
    val day = "NN"
    fun log(message: Any?) {
        println(message)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { it.split(" ").sumOf { it.toInt() } }
    }

    fun part2(input: List<String>): Int {
        return 2 * input.sumOf { it.split(" ").sumOf { it.toInt() } }
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 1)
    verify("Real part 1", part1(readInput("Day${day}")), 10)
    verify("Test part 2", part2(readInput("Day${day}_test")), 2)
    verify("Real part 2", part2(readInput("Day${day}")), 20)
}
