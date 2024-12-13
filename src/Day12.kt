import kotlin.collections.flatten
import kotlin.collections.mutableListOf

data class Plot(val region: List<Pos>, val sharedFences: Int)

fun main() {
    val day="12"
    fun log(message: Any?) {
        println(message)
    }

    fun alog(message: Any?) {
//        println(message)
    }

    fun walkFromPos(grid: Grid, pos: Pos): Plot {
//        grid.log(true)
        var targetLetter = grid.at(pos)!!
        var queue = mutableListOf<Pos>(pos)
//        var visited = mutableListOf<Pos>()
        var region = mutableListOf<Pos>()
        var sharedFences = 0
        while (queue.isNotEmpty()) {
            val next = queue.removeAt(0)
//            next.log()
            if (grid.at(next) == targetLetter) {
                region.add(next)
//                log("adding the $targetLetter at $next")
                val cardinals = grid.adjacent(next, includeDiagonal = false)
//                log("neighbors $cardinals")
                cardinals.forEach { cardinal ->
                    if (grid.at(cardinal) == targetLetter) {
                        sharedFences += 1
                        if (cardinal !in region && cardinal !in queue) {
//                        log("adding to queue $cardinal")
                            queue.add(cardinal)
                        }
                    }
                }
            }
        }
        return Plot(region, sharedFences)
    }

    fun allPlots(allPos: List<Pos>, grid: Grid): MutableList<Plot> {
        var allPlots: MutableList<Plot> = mutableListOf()
        allPos.forEach { pos ->
            if (pos !in allPlots.map { it.region }.flatten()) {
                val plot = walkFromPos(grid, pos)
                allPlots.add(plot)
    //                plot.log(">>> plot> ")
            } else {
    //                log("already visited $pos ${grid.at(pos)}")
            }
        }
        return allPlots
    }

    // grr, needed a hint
    fun coalesceSidesIntoEdges(pointsWithSideExposed: List<Pos>, groupBy: (Pos) -> Int, checkValue: (Pos) -> Int): Int {
        return pointsWithSideExposed.also { alog("1> $it") }
            .groupBy(groupBy).also { alog("2> $it") }
            .map { it.value.map(checkValue).sorted() }.also { alog("3> $it") }
            .sumOf { 1 + it.zipWithNext().count { (a, b) -> a + 1 != b } }.also { alog("4> $it") }
    }

    fun freeUnitSides(region: List<Pos>): Tuple4<List<Pos>, List<Pos>, List<Pos>, List<Pos>> {
        var topSides = mutableListOf<Pos>()
        var rightSides = mutableListOf<Pos>()
        var bottomSides = mutableListOf<Pos>()
        var leftSides = mutableListOf<Pos>()

        region.forEach { pos ->
            pos.n.let { if (it !in region) topSides.add(it) }
            pos.e.let { if (it !in region) rightSides.add(it) }
            pos.s.let { if (it !in region) bottomSides.add(it) }
            pos.w.let { if (it !in region) leftSides.add(it) }
        }
        return Tuple4(topSides, rightSides, bottomSides, leftSides)
    }

    fun sidesIntoEdges(topSides: List<Pos>, rightSides: List<Pos>, bottomSides: List<Pos>, leftSides: List<Pos>): Int {
        var edges = 0
        edges += coalesceSidesIntoEdges(topSides, groupBy = { pos: Pos -> pos.y }, checkValue = { pos: Pos -> pos.x })
        edges += coalesceSidesIntoEdges(bottomSides, groupBy = { pos: Pos -> pos.y }, checkValue = { pos: Pos -> pos.x })
        edges += coalesceSidesIntoEdges(leftSides, groupBy = { pos: Pos -> pos.x }, checkValue = { pos: Pos -> pos.y })
        edges += coalesceSidesIntoEdges(rightSides, groupBy = { pos: Pos -> pos.x }, checkValue = { pos: Pos -> pos.y })
        return edges
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val allPos = grid.allPos()
        var allPlots: MutableList<Plot> = allPlots(allPos, grid)
        return allPlots.sumOf { plot ->
            plot.region.size * ((plot.region.size * 4) - plot.sharedFences)
        }
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val allPos = grid.allPos()
        var allPlots: MutableList<Plot> = allPlots(allPos, grid)
        return allPlots.map { it.region }.sumOf { region ->
            val (topSides, rightSides, bottomSides, leftSides) = freeUnitSides(region)
            region.size * sidesIntoEdges(topSides, rightSides, bottomSides, leftSides)
        }
    }

    verify("Test part 1 t1", part1(readInput("Day${day}_test1")), 140)
    verify("Test part 1 t2", part1(readInput("Day${day}_test2")), 772)
    verify("Test part 1 t3", part1(readInput("Day${day}_test3")), 1930)
    verify("Real part 1", part1(readInput("Day${day}")), 1573474)

    verify("Test part 2 t1", part2(readInput("Day${day}_test1")), 80)
    verify("Test part 2 t2", part2(readInput("Day${day}_test2")), 436)
    verify("Test part 2 t3", part2(readInput("Day${day}_test3")), 1206)
    verify("Test part 2 t4", part2(readInput("Day${day}_test4")), 236)
    verify("Test part 2 t5", part2(readInput("Day${day}_test5")), 368)
    verify("Real part 2", part2(readInput("Day${day}")), 966476)
}
