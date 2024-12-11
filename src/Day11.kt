import jdk.internal.org.jline.utils.Colors.s

/*
    If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
    If the stone is engraved with a number that has an even number of digits, it is replaced by two stones. The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
    If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.
 */
fun main() {
    val day="11"
    fun log(message: Any?) {
        println(message)
    }

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

    fun count(stone: Long, iterations: Int): Int {
        if (iterations == 0) return 1
        return blinkStep(stone).sumOf { count(it, iterations - 1) }
    }

    fun part1(input: List<String>, steps: Int): Int {
        var list = input.first().split(" ").map(String::toLong)
        return list.sumOf { count(it, steps) }
    }

    val testResult = "2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2".split(" ").map(String::toLong)
    verify("Test part 1", part1(readInput("Day${day}_test"), steps=6), testResult.size)
    verify("Real part 1", part1(readInput("Day${day}"), steps=25), 239714)
//    verify("Real part 2", part1(readInput("Day${day}"), steps=75), 0)
}
