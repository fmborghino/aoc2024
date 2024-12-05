// LESSON HERE: read the damned question carefully :eyeroll:
// spent way too long solving this for "xmas" in any combo of adjacent zigzag moves
// left that here in case it comes up later :-)

import kotlin.collections.joinToString

enum class Dir(val x: Int, val y: Int) {
    N(0, -1),
    S(0, 1),
    E(1, 0),
    W(-1, 0),
    NE(1, -1),
    SE(1, 1),
    NW(-1, -1),
    SW(-1, 1)
}

// note to self: read the question better, spent a lot of time finding ALL the zig zag patterns, not just the straight lines
fun main() {
    val day="04"
    fun log(message: Any?) {
        println(message)
    }

    data class Pos(val x: Int, val y: Int, val depth: Int = 0)

    class Grid(rows: List<String>) {
        val deadPixel = '.'
        val target = "XMAS"
        val grid: List<List<Char>>

        init {
            grid = toGrid(rows)
        }

        fun toGrid(rows: List<String>): List<List<Char>> {
            return rows.map { str ->
                str.map { char ->
                    if (char in target) char else deadPixel
                }
            }
        }

        fun log(space: Boolean = false) {
            grid.forEach { line ->
                println(line.joinToString(if (space) " " else ""))
            }
        }

        fun move(pos: Pos, dir: Dir): Pos {
            return Pos(pos.x + dir.x, pos.y + dir.y, pos.depth)
        }

        // make a list of all the adjacent positions, but only if they're in bounds
        fun adjacent(pos: Pos): List<Pos> {
            val d = pos.depth
            return buildList<Pos> {
                // cardinals
                if (pos.x > 0) add(Pos(pos.x - 1, pos.y, d))
                if (pos.x < grid[0].size - 1) add(Pos(pos.x + 1, pos.y, d))
                if (pos.y > 0) add(Pos(pos.x, pos.y - 1, d))
                if (pos.y < grid.size - 1) add(Pos(pos.x, pos.y + 1, d))
                // diagonals
                if (pos.x > 0 && pos.y > 0) add(Pos(pos.x - 1, pos.y - 1, d))
                if (pos.x < grid[0].size - 1 && pos.y < grid.size - 1) add(Pos(pos.x + 1, pos.y + 1, d))
                if (pos.x > 0 && pos.y < grid.size - 1) add(Pos(pos.x - 1, pos.y + 1, d))
                if (pos.x < grid[0].size - 1 && pos.y > 0) add(Pos(pos.x + 1, pos.y - 1, d))
            }
        }

        fun at(pos: Pos): Char? {
            if (pos.x < 0 || pos.x >= grid[0].size || pos.y < 0 || pos.y >= grid.size) return null // out of bounds
            return grid[pos.y][pos.x]
        }
    }

    fun allTheChar(grid: Grid, char: Char): MutableList<Pos> {
        return buildList {
            grid.grid.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    if (c == char) {
                        add(Pos(x, y))
                    }
                }
            }
        }.toMutableList()
    }

    fun wordSearch(grid: Grid, word: String): Int {
        var result = 0
        val wordStart = word.first()
        val wordTail = word.substring(1)
        val allTheStarts = allTheChar(grid, wordStart)
        log("Starts of $wordStart count is ${allTheStarts.size}, searching for $wordTail")
        allTheStarts.forEach { pos ->
            Dir.entries.forEach { dir ->
//                log("$pos $dir ${dir.x} ${dir.y}")
                var nextPos = pos
                val allMatched = wordTail.all { letter ->
                    nextPos = grid.move(nextPos, dir)
                    letter == grid.at(nextPos)
                }
                if (allMatched) result += 1
            }
        }
        return result
    }

    fun crossSearch(grid: Grid): Int {
        var result = 0
        val allTheA = allTheChar(grid, 'A')
        log("there are ${allTheA.size} 'A' pivots")
        allTheA.forEach { pivot ->
            val testDiagonals = buildList<Boolean> {
                val ne = grid.at(grid.move(pivot, Dir.NE))
                val se = grid.at(grid.move(pivot, Dir.SE))
                val sw = grid.at(grid.move(pivot, Dir.SW))
                val nw = grid.at(grid.move(pivot, Dir.NW))
                add((ne == 'M' && sw == 'S') || ((ne == 'S' && sw == 'M')))
                add((nw == 'M' && se == 'S') || ((nw == 'S' && se == 'M')))
            }
            if(testDiagonals.all { diagonal -> diagonal }) {
                result += 1
            }
        }

        return result
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        grid.log()
        val result = wordSearch(grid, "XMAS")
        return result
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        grid.log()
        val result = crossSearch(grid)
        return result
    }

    verify("Test part 1", part1(readInput("Day${day}_test1")), 1)
    verify("Test part 1", part1(readInput("Day${day}_test")), 18)
    verify("Real part 1", part1(readInput("Day${day}")), -1)
    verify("Test part 2", part2(readInput("Day${day}_test")) , 9)
    verify("Real part 2", part2(readInput("Day${day}")), 2005)

    // === abandoned code below ===
    fun allTheChar(grid: Grid, positions: List<Pos>, char: Char): List<Pos> {
        val matchingNeighbors: List<Pos> = positions.flatMap { pos -> grid.adjacent(pos) }.filter { grid.at(it) == char }
        return matchingNeighbors
    }

    // searches grid for a word in any zigzag pattern (NYT Strands style)
    fun bfsWordStrands(grid: Grid, word: String): Int {
        val targets = word.toCharArray()
        var found = 0
        val queue = allTheChar(grid, targets[0])
        while (queue.isNotEmpty()) {
            val pos = queue.removeAt(0)
            if (grid.at(pos) == targets[pos.depth]) {
                if (pos.depth == targets.size - 1) {
                    found += 1
                } else {
                    val newPos = Pos(pos.x, pos.y, pos.depth + 1)
                    log("from: $pos, ${grid.at(pos)}")
                    grid.adjacent(newPos).forEach { neighbor ->
                        log("   > $neighbor ${grid.at(neighbor)}")
                        queue.add(neighbor)
                    }
                }
            }
        }
        return found
    }

    // this is also a Strands style search, again not what the question was asking for
    fun joinTheDots(grid: Grid): Int {
        val allTheX = allTheChar(grid, 'X')
        val allTheM = allTheChar(grid, allTheX, 'M')
        val allTheA = allTheChar(grid, allTheM, 'A')
        val allTheS = allTheChar(grid, allTheA, 'S')
        return allTheS.size
    }

    fun mainAbandoned(grid: Grid) {
        val r1 =  bfsWordStrands(grid, "XMAS") // test 239, data 51669
        val r2 = joinTheDots(grid) // test 239, data 51669
    }
}
