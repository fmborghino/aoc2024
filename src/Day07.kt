data class Equation(val total: Long, val args: List<String>)

fun main() {
    val day = "07"
    fun log(message: Any?) {
        println(message)
    }

    fun parse(input: List<String>): List<Equation> {
        val r = mutableListOf<Equation>()
        input.forEach { line ->
            r.add(
                Equation(
                    line.substringBefore(":").toLong(),
                    line.substringAfter(":").trim().split(" ")
                )
            )
        }
        return r
    }

    fun generateCalcCombos(args: List<String>, ops: List<String>): List<List<String>> {
        if (args.size < 2) return emptyList()

        val results = mutableListOf<List<String>>()

        fun backtrack(current: MutableList<String>, index: Int) {
            if (index == args.size - 1) {
                results.add(current.toList())
                return
            }

            for (op in ops) {
                current.add(op)
                current.add(args[index + 1])
                backtrack(current, index + 1)
                current.removeLast()
                current.removeLast()
            }
        }

        backtrack(mutableListOf(args[0]), 0)

        return results
    }

    fun calculate(tokens: List<String>): Long {
        var result = 0L
        var operation = "+"
        tokens.forEach { token ->
//            log("$token $result")
            when (token) {
                "+", "*", "||" -> operation = token
                else -> {
                    val operand = token.toLong()
                    when (operation) {
                        "+" -> result += operand
                        "*" -> result *= operand
                        "||" -> result = (result.toString() + operand.toString()).toLong()
                    }
                }
            }
        }
        return result
    }

    fun part1(input: List<String>, operands: List<String>): Long {
        val inputs = parse(input)
        var result = 0L
        inputs.forEach inputs@{ input ->
//            input.log()
            val calcCombos = generateCalcCombos(input.args, operands)
            calcCombos.forEach { calcCombo ->
//                calcCombo.log()
                val r = calculate(calcCombo)
                if (input.total == r) {
                    result += r
                    return@inputs // early exit if we found a solution
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        return 2 * input.sumOf { it.toInt() }
    }

    verify("Test part 1", part1(readInput("Day${day}_test"), listOf("*", "+")), 3749)
    verify("Real part 1", part1(readInput("Day${day}"), listOf("*", "+")), 1708857123053)
    verify("Test part 2", part1(readInput("Day${day}_test"), listOf("*", "+", "||")), 11387)
    verify("Real part 2", part1(readInput("Day${day}"), listOf("*", "+", "||")), 189207836795655)
}
