import kotlin.collections.map

data class Bot(val px: Int, val py: Int, val vx: Int, val vy: Int)

typealias BotMap = Map<Pair<Int, Int>, List<Bot>>

fun main() {
    val day = "14"
    fun log(message: Any?) {
        println(message)
    }

    val REGEXP = """^p=(-?\d{1,3}),(-?\d{1,3}) v=(-?\d{1,3}),(-?\d{1,3})""".toRegex()

    fun moveBots(bots: List<Bot>, steps: Int = 1, width: Int, height: Int): List<Bot> {
        return bots.map { bot ->
            val px = wrap(bot.px + (bot.vx * steps), width)
            val py = wrap(bot.py + (bot.vy * steps), height)
            Bot(px, py, bot.vx, bot.vy)
        }
    }

    fun quadrants(bots: List<Bot>, width: Int, height: Int): Tuple4<Int, Int, Int, Int> {
        var nw = 0
        var ne = 0
        var se = 0
        var sw = 0
        val midWidth = width / 2
        val midHeight = height / 2
        bots.forEach { bot ->
            when {
                bot.px in 0..<midWidth           && bot.py in 0..<midHeight -> nw += 1
                bot.px in midWidth + 1 ..< width && bot.py in 0..<midHeight -> ne += 1
                bot.px in midWidth + 1 ..< width && bot.py in midHeight + 1 ..< height  -> se += 1
                bot.px in 0..<midWidth           && bot.py in midHeight + 1 ..< height -> sw += 1
            }
        }

        return Tuple4(nw, ne, se, sw)
    }

    fun parse(input: List<String>): List<Bot> {
        return input.map { line ->
            val (px, py, vx, vy) = REGEXP.find(line)?.destructured
                ?: throw IllegalArgumentException("not match on: $line")
            Bot(px.toInt(), py.toInt(), vx.toInt(), vy.toInt())
        }
    }

    fun part1(input: List<String>, width: Int, height: Int): Int {
        val bots = parse(input)
        val movedBots = moveBots(bots, steps=100, width, height)
        val (nw, ne, se, sw) = quadrants(movedBots, width, height)
        return nw * ne * se * sw
    }

    // guess we hit the easter egg when there are no overlaps (ugh)
    fun isEasterEgg(bots: Map<Pair<Int, Int>, List<Bot>>): Boolean {
        return bots.all { it.value.size == 1 }
    }

    fun printGrid(seconds: Int, botMap: BotMap, width: Int, height: Int) {
        println("=== $seconds")
        for (y in 0..<height) {
            for (x in 0..<width) {
                val bots: Int? = botMap[Pair(x, y)]?.size
                when (bots) {
                    null -> print(".")
//                    1 -> print("#")
                    else -> print(bots)
                }
            }
            println()
        }
    }

    fun part2(input: List<String>, width: Int, height: Int): Int {
        var bots = parse(input)
        var botMap: BotMap = bots.groupBy { Pair(it.px, it.py) }
        var seconds = 0
//        printGrid(seconds, botMap, width, height)

        do {
            bots = moveBots(bots, steps=1, width=width, height=height)
            botMap = bots.groupBy { Pair(it.px, it.py) }
            seconds += 1
//            printGrid(seconds, botMap, width, height)
        } while (!isEasterEgg(botMap))

        printGrid(seconds, botMap, width, height)
        return seconds
    }

    verify("Test part 1", part1(readInput("Day${day}_test"), width = 11, height = 7), 12)
    verify("Real part 1", part1(readInput("Day${day}"), width = 101, height = 103), 226236192)
    // no part2 test
    verify("Real part 2", part2(readInput("Day${day}"), width = 101, height = 103), 8168)
}
