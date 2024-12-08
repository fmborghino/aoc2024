fun main() {
    val day="08"
    fun log(message: Any?) {
        println(message)
    }

    fun scanLetters(grid: Grid): Map<Char, List<Pos>> {
        return buildMap<Char, MutableList<Pos>> {
            for (y in 0..<grid.height) {
                for (x in 0..<grid.width) {
                    val pos = Pos(x, y)
                    val at = grid.at(pos)
                    if (at != null && at != '.') {
//                        log("$pos $at")
                        getOrPut(at) { mutableListOf() }.add(pos)
                    }
                }
            }
        }
    }


    fun calculateExtendedEndpoints(posA: Pos, posB: Pos): List<Pos> {
        val dx = posB.x - posA.x
        val dy = posB.y - posA.y

        val endpoint1 = Pos(posA.x - dx, posA.y - dy)
        val endpoint2 = Pos(posB.x + dx, posB.y + dy)

        return listOf(endpoint1, endpoint2)
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
//        grid.log(true)
        val letterToPos = scanLetters(grid)
//        letterToPos.log()

        val ledger = mutableListOf<Pos>()
        letterToPos.forEach { letter, posList ->
            val pairs = uniquePairs(posList)
//            pairs.log()
            pairs.forEach { posPair ->
                val endPoints = calculateExtendedEndpoints(posPair.first, posPair.second)
//                endPoints.log("$posPair -> ")
                endPoints.forEach { endPoint ->
                    val c = grid.at(endPoint)
                    if (c != null) {
                        grid.set(endPoint, '#')
                        ledger.add(endPoint)
                    }
                }
            }
//            grid.log()
        }
        return ledger.toSet().size
    }

    fun part2(input: List<String>): Int {
        return 2 * input.sumOf { it.toInt() }
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 14)
//    verify("Real part 1", part1(readInput("Day${day}")), 392)
//    verify("Test part 2", part2(readInput("Day${day}_test")) , 34)
//    verify("Real part 2", part2(readInput("Day${day}")), 1200)
}
