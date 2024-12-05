import jdk.jfr.internal.consumer.EventLog.update

fun main() {
    val day="05"
    fun log(message: Any?) {
        println(message)
    }

    fun parse(input: List<String>): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val rules = mutableListOf<Pair<Int, Int>>()
        val updates = mutableListOf<List<Int>>()

        input.forEach { line ->
            if (line.trim().isEmpty()) return@forEach
            when (line[2]) {
                '|' -> rules.add(Pair(line.substringBefore("|").toInt(), line.substringAfter("|").toInt()))
                ',' -> updates.add(line.split(",").map(String::toInt))
                else -> error("input line wonky: [$line]")
            }
        }

        return Pair(rules, updates)
    }

    fun rulePositionsInUpdate(rule: Pair<Int, Int>, update: List<Int>): Pair<Int, Int> {
        return Pair(update.indexOf(rule.first), update.indexOf(rule.second))
    }

    fun triageUpdates(
        updates: List<List<Int>>,
        rules: List<Pair<Int, Int>>
    ): Pair<List<List<Int>>, List<List<Int>>> {
        val goodUpdate = mutableListOf<List<Int>>()
        val poorUpdate = mutableListOf<List<Int>>()
        updates.forEachIndexed updates@{ i, update ->
            update.log()
            rules.forEach rules@{ rule ->
                val (a, b) = rulePositionsInUpdate(rule, update)
                if (a == -1 || b == -1) {
                    log("skip rule $rule ($a, $b)")
                    return@rules
                }
                if (a > b) {
                    log("${i + 1} SKIP update (rule ordering) $rule")
                    poorUpdate.add(update)
                    return@updates
                }
                log("rule was good $rule ($a, $b)")
            }
            log("${i + 1} KEEP update $update")
            goodUpdate.add(update)
        }
        return Pair(goodUpdate, poorUpdate)
    }

    fun part1(input: List<String>): Int {
        val (rules, updates) = parse(input)

        val (goodUpdate, poorUpdate) = triageUpdates(updates, rules)

        val middles = goodUpdate.map { update ->
            val middleIndex = update.size / 2
            update[middleIndex]
        }

        return middles.sum()
    }

    fun part2(input: List<String>): Int {
        return 2 * input.sumOf { it.toInt() }
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 143)
//    verify("Real part 1", part1(readInput("Day${day}")), 4996)
//    verify("Test part 2", part2(readInput("Day${day}_test")) , 123)
//    verify("Real part 2", part2(readInput("Day${day}")), 1200)
}
