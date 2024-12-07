import kotlin.to

typealias PosDir = Pair<Pos, Dir>

fun main() {
    val day="06"
    fun log(message: Any?) {
//        println(message)
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

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val allPosDir = mutableListOf<PosDir>()
//        grid.log(true)
        var guardPos = grid.find('^')
        var guardDir = dirMap[grid.at(guardPos)]
        log("guard at $guardPos going $guardDir")
        var steps = 1
        while (true) {
            allPosDir.add(Pair(guardPos, guardDir!!))
            grid.set(guardPos, 'X')
            val nextPos = grid.move(guardPos, guardDir!!)
            val nextChar = grid.at(nextPos)
            when (nextChar) {
                null -> {
                    log("step out going from $guardPos to $nextPos heading $guardDir")
                    return steps
                }
                '#' -> {
                    log("turn at $guardPos from $guardDir to turn[guardDir]")
                    guardDir = turnRight[guardDir]
                }
                '.', 'X' -> {
                    log("move from $guardPos to $nextPos")
                    if (nextChar == '.') steps += 1 // don't count already visited positions
                    guardPos = nextPos
                }
                else -> error("oops wonky char $nextChar at $nextPos")
            }
        }
        return -1 // not reached
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 41)
    verify("Real part 1", part1(readInput("Day${day}")), 5086)
//    verify("Test part 2", part2(readInput("Day${day}_test")), 6)
//    verify("Real part 2", part2(readInput("Day${day}")), 1200)
}
