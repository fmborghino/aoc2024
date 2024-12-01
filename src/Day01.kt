import kotlin.math.abs

fun main() {
    fun pairs(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val ar1 = mutableListOf<Int>()
        val ar2 = mutableListOf<Int>()
        for (line in input) {
            val numbers = line.trim().split("\\s+".toRegex()).map { it.toInt() }
            ar1.add(numbers[0])
            ar2.add(numbers[1])

            // another way
//            val left = line.substringBefore(" ").toInt()
//            val right = line.substringAfterLast(" ").toInt()
        }

        return Pair(ar1, ar2) // aka `ar1 to ar2`
    }

    fun part1(input: List<String>): Int {
        val (ar1, ar2) = pairs(input)
        ar1.sort()
        ar2.sort()

        val ar3 = IntArray(ar1.size) { index ->
            abs(ar1[index] - ar2[index])
        }

        return ar3.sum()
    }

    fun part2(input: List<String>): Int {
        val (ar1, ar2) = pairs(input)
        val counts: Map<Int, Int> = ar2.groupingBy { it }.eachCount()

        val ar3 = ar1.map { it * counts.getOrDefault(it, 0) }
        return ar3.sum()
    }

    verify("Test part 1", part1(readInput("Day01_test")), 11)

    verify("Puzzle part 1", part1(readInput("Day01")), 1882714)

    verify("Test part 2", part2(readInput("Day01_test")) , 31)

    verify("Puzzle part 2", part2(readInput("Day01")), 19437052)
}
