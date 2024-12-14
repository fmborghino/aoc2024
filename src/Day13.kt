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

    fun cramer(machine: Machine, offset: Long): Long {
        with(machine) {
            val px = p.x + offset
            val py = p.y + offset
            val d = (a.x * b.y - a.y * b.x)
            val pressA = (px * b.y - py * b.x) / d
            val pressB = (a.x * py - a.y * px) / d

            val dX = pressA * a.x + pressB * b.x
            val dY = pressA * a.y + pressB * b.y

            return if (dX == px && dY == py) 3 * pressA + pressB else 0
        }
    }

    fun part1(input: List<String>, offset: Long = 0): Long {
        return parse(input).sumOf { cramer(it, offset) }
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 480)
    verify("Real part 1", part1(readInput("Day${day}")), 39996)
    verify("Real part 2", part1(readInput("Day${day}"), offset=10_000_000_000_000L), 73267584326867)
}
