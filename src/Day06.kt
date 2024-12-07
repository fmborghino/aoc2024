import java.lang.invoke.MethodHandles.loop
import kotlin.to

data class PosDir(val pos: Pos, val dir: Dir)

class StrictMap<K, V>(private val map: Map<K, V>) {
    operator fun get(key: K): V {
        return map[key] ?: throw NoSuchElementException("key $key is missing")
    }
}

fun main() {
    val day="06"
    fun log(message: Any?) {
//        println(message)
    }

    val dirMap = StrictMap(mapOf<Char, Dir>(
        '^' to Dir.N,
        '>' to Dir.E,
        'v' to Dir.S,
        '<' to Dir.W,
    ))

    val turnRight = StrictMap(mapOf<Dir, Dir>(
        Dir.N to Dir.E,
        Dir.E to Dir.S,
        Dir.S to Dir.W,
        Dir.W to Dir.N,
    ))

    fun walk(grid: Grid, startPos: Pos, startDir: Dir, part: Int = 1): Int {
        val ledger = mutableListOf<PosDir>()
        var guardPos = startPos
        var guardDir = startDir

        while (true) {
            val posDir = PosDir(guardPos, guardDir)
            if (part == 2 && posDir in ledger) return 1
            ledger.add(posDir)
            grid.set(guardPos, 'X') // leave a trail of breadcrumbs
            val nextPos = grid.move(guardPos, guardDir)
            val nextChar = grid.at(nextPos)
            when (nextChar) {
                null -> {
                    log("step out going from $guardPos to $nextPos heading $guardDir")
                    // ie how many steps, and don't recount revisited positions
                    return if (part == 1) ledger.distinctBy { it.pos }.size else 0
                }
                '#', 'O' -> {
                    log("turn at $guardPos from $guardDir to turn[guardDir]")
                    guardDir = turnRight[guardDir]
                }
                '.', 'X', '^' -> {
                    log("move from $guardPos to $nextPos")
                    guardPos = nextPos
                }
                else -> error("oops wonky char $nextChar at $nextPos")
            }
        }
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
//        grid.log(true)
        var guardPos = grid.find('^')
        var guardDir = dirMap['^']
        log("guard at $guardPos going $guardDir")
        val result = walk(grid, guardPos, guardDir)
        return result
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        var guardPos = grid.find('^')
        var guardDir = dirMap['^']

        var loops = 0
        val loopMax = grid.height * grid.width
        var count = 1
        for (y in 0..<grid.height) {
            for (x in 0..<grid.width) {
                if (count % 100 == 0) println("$count of $loopMax, found $loops")
                val gridCopy: Grid = grid.copy()
                gridCopy.set(Pos(x, y), 'O')
                loops += walk(gridCopy, guardPos, guardDir, part = 2)
                count += 1
            }
        }

        return loops
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 41)
    verify("Real part 1", part1(readInput("Day${day}")), 5086)
    verify("Test part 2", part2(readInput("Day${day}_test")), 6)
    println("go make coffee this is gonna take a few minutes...")
    verify("Real part 2", part2(readInput("Day${day}")), 1770)
}
