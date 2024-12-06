import kotlin.to

fun main() {
    val day="06"
    fun log1(message: Any?) {
//        println(message)
    }
    fun log2(message: Any?) {
        println(message)
    }

    val dirMap = mapOf<Char, Dir>(
        '^' to Dir.N,
        '>' to Dir.E,
        'v' to Dir.S,
        '<' to Dir.W,
    )

    val turnRight = mapOf<Dir, Dir>(
        Dir.N to Dir.E,
        Dir.E to Dir.S,
        Dir.S to Dir.W,
        Dir.W to Dir.N,
    )

    fun part1and2(input: List<String>): Pair<Int, Int> {
        val grid = Grid(input)
        val allPosDir = mutableListOf<Pair<Pos, Dir>>()
//        grid.log(true)
        var guardPos = grid.find('^')
        var guardDir = dirMap[grid.at(guardPos)]
        log1("guard at $guardPos going $guardDir")
        var steps = 1
        var loopies = 0
        while (true) {
            allPosDir.add(Pair(guardPos, guardDir!!))
            grid.set(guardPos, 'X')
            val nextPos = grid.move(guardPos, guardDir!!)
//            nextPos.log()
            val nextChar = grid.at(nextPos)
//            nextChar.log()
            when (nextChar) {
                null -> {
                    log1("step out going from $guardPos to $nextPos heading $guardDir")
                    return Pair(steps, loopies)
                }
                '#' -> {
                    log1("turn at $guardPos from $guardDir to turn[guardDir]")
                    guardDir = turnRight[guardDir]
                }
                '.', 'X' -> {
                    log1("move from $guardPos to $nextPos")
                    if (nextChar == '.') steps += 1 // don't count already visited positions
                    guardPos = nextPos
//                    grid.log()
                }
                else -> error("oops wonky char $nextChar at $nextPos")
            }
        }
        return Pair(-1, -1) // not reached
    }

    verify("Test part 1", part1and2(readInput("Day${day}_test")).first, 41)
    verify("Real part 1", part1and2(readInput("Day${day}")).first, 5086)
//    verify("Test part 2", part1and2(readInput("Day${day}_test")).second , 6)
//    verify("Real part 2", part1and2(readInput("Day${day}")).second, 1200)
}
