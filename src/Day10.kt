fun main() {
    val day="10"
    fun log(message: Any?) {
        println(message)
    }

    // counts paths from '0' to '9', return a ledger of the '9' positions
    fun bfsPaths(grid: Grid, start: Pos): Pair<Set<Pos>, List<Pos>> {
        var foundNinesList = mutableListOf<Pos>()
        var foundNinesSet = mutableSetOf<Pos>()
        val queue = mutableListOf<Pos>(start)
        while (queue.isNotEmpty()) {
            val pos = queue.removeAt(0)
            if (grid.at(pos) == '9') {
                foundNinesList.add(pos)
                foundNinesSet.add(pos)
            } else {
                val neighbors = grid.adjacent(pos, includeDiagonal = false)
                neighbors
                    .filter { neighbor -> grid.at(neighbor)!! - grid.at(pos)!! == 1 } // 1 away? (Char math works here!)
                    .forEach { neighbor -> queue.add(neighbor) }
            }
        }
        return Pair(foundNinesSet, foundNinesList)
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
//        grid.log(true)
        val starts = grid.findAll('0')
//        starts.log()
        return starts.sumOf { pos ->
            val paths = bfsPaths(grid, pos).first.size
//            paths.log()
            paths
        }
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val starts = grid.findAll('0')
        return starts.sumOf { pos ->
            val paths = bfsPaths(grid, pos).second.size
//            paths.log()
            paths
        }    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 36)
    verify("Real part 1", part1(readInput("Day${day}")), 682)
    verify("Test part 2", part2(readInput("Day${day}_test")) , 81)
    verify("Real part 2", part2(readInput("Day${day}")), 1511)
}
