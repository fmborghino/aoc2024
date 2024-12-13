import kotlin.collections.flatten
import kotlin.collections.mutableListOf

data class Plot(val region: List<Pos>, val sharedFences: Int)

fun main() {
    val day="12"
    fun log(message: Any?) {
        println(message)
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

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
//        grid.log(true)
        val allPos = grid.allPos()
//        allPos.log()
        var allPlots: MutableList<Plot> = allPlots(allPos, grid)
        var result = 0
        allPlots.forEach { plot ->
//            log(plot)
            result += plot.region.size * ((plot.region.size * 4) - plot.sharedFences)
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val allPos = grid.allPos()
        var allPlots: MutableList<Plot> = allPlots(allPos, grid)

        return 0
    }

    verify("Test part 1 t1", part1(readInput("Day${day}_test1")), 140)
    verify("Test part 1 t2", part1(readInput("Day${day}_test2")), 772)
    verify("Test part 1 t3", part1(readInput("Day${day}_test3")), 1930)
    verify("Real part 1", part1(readInput("Day${day}")), 1573474)

//    verify("Test part 2 t1", part1(readInput("Day${day}_test1")), 80)
//    verify("Test part 2 t2", part1(readInput("Day${day}_test2")), 436)
//    verify("Test part 2 t3", part1(readInput("Day${day}_test3")), 1206)
//    verify("Test part 2 t4", part1(readInput("Day${day}_test4")), 236)
//    verify("Test part 2 t5", part1(readInput("Day${day}_test5")), 368)
//    verify("Real part 2", part2(readInput("Day${day}")), 1200)
}
