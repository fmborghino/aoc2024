import kotlin.math.abs

fun main() {
    fun log(message: Any?) {
        println(message)
    }

    fun pairOk(a: Int, b: Int): Boolean {
        val d = abs(a - b)
        log("[$a, $b] $d")
        return (d >= 1 && d <= 3)
    }

    fun normalizeAscending(line: String): List<Int> {
        line.log()
        val nums = line.split(" ").map { it -> it.toInt() }
        return if (nums[0] < nums[1]) nums else nums.reversed()
    }

    fun strictlyAscending(nums: List<Int>): Boolean {
        val isStrict = nums == nums.sorted()
        if (!isStrict) log("NOT strict, skip")
        return isStrict
    }

    fun part1(input: List<String>): Int {
        var good = 0
        for (line in input) {
            line.log()
            val nums = normalizeAscending(line)
            val isSafe = strictlyAscending(nums) && nums.windowed(size = 2, step = 1).all { it ->
                pairOk(it[0], it[1])
            }
            log("safe $isSafe")
            if (isSafe) good++
        }

        return good
    }

    fun possibles(line: List<Int>): List<List<Int>> {
        val p = mutableListOf<List<Int>>()
        p.add(line)
        for (i in line.indices) {
            p.add(line.omitAt(i))
        }
        p.log()
        return p
    }

    fun part2(input: List<String>): Int {
        var good = 0
        for (line in input) {
            log("> $line")
            val isSafe = possibles(line.split(" ").map { it -> it.toInt() }).any { possible ->
                val normalized = if (possible[0] < possible[1]) possible else possible.reversed()
                strictlyAscending(normalized) &&
                        normalized.windowed(size = 2, step = 1).all { it ->
                            pairOk(it[0], it[1])
                        }
            }
            log("safe $isSafe")
            if (isSafe) good++
        }

        return good
    }

    verify("Test part 1", part1(readInput("Day02_test")), 2)
    verify("Data part 1", part1(readInput("Day02")), 314)
    verify("Test part 2", part2(readInput("Day02_test")), 4)
    verify("Data part 2", part2(readInput("Day02")), 373)
}
