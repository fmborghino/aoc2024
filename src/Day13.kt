import kotlin.collections.map

data class Machine(val a: Pos, val b: Pos, val p: Pos)

val re = """X[=+](\d+), Y[=+](\d+)""".toRegex()

fun main() {
    val day = "13"
    fun log(message: Any?) {
        println(message)
    }

    fun parse(input: List<String>): List<Machine> {
        var a: Pos = NullPos
        var b: Pos = NullPos
        var p: Pos
        return buildList {
            input.map { line ->
                when {
                    line.startsWith("Button A:") -> {
                        val (x, y) = re.find(line)?.destructured ?: error("oops: $line")
                        a = Pos(x.toInt(), y.toInt())
                    }

                    line.startsWith("Button B:") -> {
                        val (x, y) = re.find(line)?.destructured ?: error("oops: $line")
                        b = Pos(x.toInt(), y.toInt())
                    }

                    line.startsWith("Prize:") -> {
                        val (x, y) = re.find(line)?.destructured ?: error("oops: $line")
                        p = Pos(x.toInt(), y.toInt())
                        // this only works because of the input file ordering invariants
                        add(Machine(a, b, p))
                    }

                    line.isEmpty() -> {}
                    else -> error("oops: $line")
                }
            }
        }
    }

    fun cramer(machine: Machine): Int {
        with(machine) {
            val d = (a.x * b.y - a.y * b.x)
            val pressA = (p.x * b.y - p.y * b.x) / d
            val pressB = (a.x * p.y - a.y * p.x) / d

            val dX = pressA * a.x + pressB * b.x
            val dY = pressA * a.y + pressB * b.y

            return if (dX == p.x && dY == p.y) 3 * pressA + pressB else 0
        }
    }

    fun part1(input: List<String>): Int {
        return parse(input).sumOf { cramer(it) }
    }

    fun part2(input: List<String>): Int {
        return 2 * input.sumOf { it.toInt() }
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 480)
    verify("Real part 1", part1(readInput("Day${day}")), 39996)
//    verify("Test part 2", part2(readInput("Day${day}_test")), 2)
//    verify("Real part 2", part2(readInput("Day${day}")), 20)
}
