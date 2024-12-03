fun main() {
    fun log(message: Any?) {
        println(message)
    }

    val day = "03"
    val regexMul = """mul\(\d{1,3},\d{1,3}\)""".toRegex()

    fun inputToMuls(input: String): List<String> {
        val sums = regexMul.findAll(input).map { it.value }.toList()
        sums.log()
        return sums
    }

    fun mulsToPairs(muls: List<String>): List<Pair<Int, Int>> {
        val pairs = muls
            .map { mul ->
                val nums = mul.substringAfter("mul(").substringBeforeLast(")")
                val (a, b) = nums.split(",")
                a.toInt() to b.toInt()
            }
        pairs.log()
        return pairs
    }

    fun calc(pairs: List<Pair<Int, Int>>): Long {
        return pairs.fold(0L) { acc, pair ->
            acc + (pair.first.toLong() * pair.second.toLong())
        }
    }

    fun nukeJunk(input: String): String {
        input.log()
        val regexJunk = Regex("""don't\(\).*?do\(\)""")
//        val matches = regexJunk.findAll(input)
//        matches.forEach { match ->
//            println("Removed section: ${match.value}")
//        }
        val nuked = input.replace(regexJunk, "")
        val nukedAndTailed = nuked.substringBeforeLast("don't()")
        nukedAndTailed.log()
        return nukedAndTailed
    }

    fun part1(input: List<String>): Long {
        val sums = inputToMuls(input.joinToString())
        val pairs = mulsToPairs(sums)
        return calc(pairs)
    }

    fun part2(input: List<String>): Long {
        val inputs = nukeJunk(input.joinToString())
        val sums = inputToMuls(inputs)
        val pairs = mulsToPairs(sums)
        return calc(pairs)
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 161L)
    verify("Real part 1", part1(readInput("Day${day}")), 184122457L)
    verify("Test part 2", part2(readInput("Day${day}_test2")) , 48L)
    verify("Real part 2", part2(readInput("Day${day}")), 107862689L)
}
