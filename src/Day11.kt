fun main() {
    val day="11"
    fun log(message: Any?) {
        println(message)
    }

    var countMemo = mutableMapOf<Pair<Long, Int>, Long>()

    fun blinkStep(stone: Long): List<Long> {
        if (stone == 0L) return listOf(1L)
        val s = stone.toString()
        if (s.length % 2 == 0) {
            val mid = s.length / 2
            val a = s.substring(0, mid)
            val b = s.substring(mid).trimStart { it == '0' }
            return listOf(a.toLong(), if (b == "") 0L else b.toLong())
        }
        return listOf(stone * 2024L)
    }

    fun count(stone: Long, iterations: Int): Long {
        if (iterations == 0) return 1
        val key = Pair(stone, iterations)
        if (countMemo.containsKey(key)) return countMemo[key]!!
        val r = blinkStep(stone).sumOf { count(it, iterations - 1) }
        countMemo[key] = r
        return r
    }

    fun part1(input: List<String>, steps: Int): Long {
        var list = input.first().split(" ").map(String::toLong)
        return list.sumOf { count(it, steps) }
    }

    val testResult = "2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2".split(" ").map(String::toLong)
    verify("Test part 1", part1(readInput("Day${day}_test"), steps=6), testResult.size.toLong())
    verify("Real part 1", part1(readInput("Day${day}"), steps=25), 239_714)
    verify("Real part 2", part1(readInput("Day${day}"), steps=75), 284_973_560_658_514)
}
