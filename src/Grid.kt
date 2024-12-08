data class Pos(val x: Int, val y: Int)

val NullPos = Pos(-1, -1)

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

class Grid(rows: List<String>) {
    var grid: MutableList<MutableList<Char>>

    init {
        grid = toGrid(rows)
    }

    val width: Int
        get() = if (grid.isNotEmpty()) grid[0].size else 0

    val height: Int
        get() = grid.size

    fun toGrid(rows: List<String>, includes: String? = null, deadPixel: Char = '.'): MutableList<MutableList<Char>> {
        return rows.map { str ->
            str.map { char ->
                if (includes == null || includes.contains(char) == true ) char else deadPixel
            }.toMutableList()
        }.toMutableList()
    }

    fun copy(): Grid {
        val newGridData = grid.map { it.toMutableList() }.toMutableList()
        val newGrid = Grid(emptyList())
        newGrid.grid = newGridData
        return newGrid
    }

    fun log(space: Boolean = false) {
        grid.forEach { line ->
            println(line.joinToString(if (space) " " else ""))
        }
    }

    fun move(pos: Pos, dir: Dir): Pos {
        return Pos(pos.x + dir.x, pos.y + dir.y)
    }

    // make a list of all the adjacent positions, but only if they're in bounds
    fun adjacent(pos: Pos): List<Pos> {
        return buildList<Pos> {
            // cardinals
            if (pos.x > 0) add(Pos(pos.x - 1, pos.y))
            if (pos.x < grid[0].size - 1) add(Pos(pos.x + 1, pos.y))
            if (pos.y > 0) add(Pos(pos.x, pos.y - 1))
            if (pos.y < grid.size - 1) add(Pos(pos.x, pos.y + 1))
            // diagonals
            if (pos.x > 0 && pos.y > 0) add(Pos(pos.x - 1, pos.y - 1))
            if (pos.x < grid[0].size - 1 && pos.y < grid.size - 1) add(Pos(pos.x + 1, pos.y + 1))
            if (pos.x > 0 && pos.y < grid.size - 1) add(Pos(pos.x - 1, pos.y + 1))
            if (pos.x < grid[0].size - 1 && pos.y > 0) add(Pos(pos.x + 1, pos.y - 1))
        }
    }

    fun at(pos: Pos): Char? {
        if (pos.x < 0 || pos.x >= grid[0].size || pos.y < 0 || pos.y >= grid.size) return null // out of bounds
        return grid[pos.y][pos.x]
    }

    fun set(pos: Pos, newChar: Char) {
        if (pos.y in grid.indices && pos.x in grid[pos.y].indices) {
            grid[pos.y][pos.x] = newChar
        } else {
            error("grid.set(): invalid coordinates at $pos")
        }
    }

    fun find(target: Char): Pos {
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == target) {
                    return Pos(x, y)
                }
            }
        }
        return NullPos
    }

    fun allPos(): List<Pos> {
        return buildList<Pos> {
            for (y in grid.indices) {
                for (x in grid[y].indices) {
                    add(Pos(x, y))
                }
            }
        }
    }
}
