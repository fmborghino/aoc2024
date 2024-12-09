typealias Id = Int
const val FREE: Id = -1

fun main() {
    val day="09"
    fun log(message: Any?) {
//        println(message)
    }

    fun logblock(block: List<Id>, msg: String = "") {
        val s: String = block.joinToString("") { if (it == -1) "." else it.toString() }
//        println(msg + s)
    }

    fun toDmap(disk: List<Int>): MutableList<Id> {
        var blocks = mutableListOf<Id>()
//        disk.log()
        var id = 0 // current file id

        disk.windowed(2, 2).forEach { (file, free) ->
//            log("$file > $free")
            blocks.addAll(List(file) { id })
            blocks.addAll(List(free) { FREE })
            id += 1
        }
        if (disk.size % 2 != 0) { // snag that last file (with no trailing free count)
            val file = disk.last()
            blocks.addAll(List(file) { id })
        }
        return blocks
    }

    fun findFirst(list: List<Int>, start: Int, target: Int): Int {
        if (start !in list.indices) return -1
        for (i in start until list.size) {
            if (list[i] == target) return i
        }
        return -1
    }

    fun findFirstOfSize(list: List<Int>, start: Int, target: Int, size: Int): Int {
        val spanTarget = List(size) { target }
        return list.indexOfSublist(spanTarget)
    }

    fun compact1(blocks: MutableList<Int>) {
        var lastFreePosition = findFirst(blocks, 0, FREE)
        for (i in blocks.size - 1 downTo 0) {
            val moveBlock = blocks[i]
            val nextFree = findFirst(blocks, lastFreePosition, FREE)
            if (nextFree > i) return // our next FREE block is in the end-zone

            if (blocks[i] != FREE) {
                blocks[nextFree] = moveBlock
                blocks[i] = FREE
//                log("moved $nextFree <-> $i ")
            }
        }
    }

    fun checksum(blocks: List<Id>): Long {
        return blocks.map { it.toLong() }.withIndex().filter { it.value != -1L }.sumOf { it.value * it.index  }.toLong()
    }


    fun replaceListInList(main: MutableList<Int>, replace: List<Int>, at: Int) {
        for (i in replace.indices) {
            main[at + i] = replace[i]
        }
    }

    fun findLastFile(blocks: List<Int>, from: Int): Triple<Int, Int, Int> {
        if (from !in blocks.indices) return Triple(-1, -1, -1)

        var firstIndex = -1
        var count = 0
        var target: Int? = null

        for (i in from downTo 0) {
            val current = blocks[i]
            if (current == -1) continue // skip

            if (target == null) {
                // first non -1
                target = current
                firstIndex = i
                count++
            } else if (current == target) {
                count++
            } else {
                // found something other than target
                break
            }
        }

        return if (target != null) Triple(target, count, firstIndex - count + 1) else Triple(-1, -1, -1)
    }

    fun compact2(blocks: MutableList<Int>) {
        var lastFromBackFilePosition = blocks.size - 1
        do {
            var (id, size, from) = findLastFile(blocks, lastFromBackFilePosition)
            lastFromBackFilePosition = from - 1
            if (id != -1) log("found> ${(List(size) { id }).joinToString("") } at $from")
            if (id == -1) {
                log("found> end condition")
                return
            }
            val freePosition = findFirstOfSize(blocks, 0, FREE, size)
            logblock(blocks, "a>")
            if (freePosition > -1 && freePosition < lastFromBackFilePosition) {
                replaceListInList(blocks, MutableList(size) { id }, freePosition)
                replaceListInList(blocks, MutableList(size) { FREE }, from)
                logblock(blocks, "b>")
            } else {
                log("b> no space")
            }
        } while (true)
    }

    fun part1(input: List<String>, part: Int=1): Long {
//        input.log("input ")
        var disk = input[0].map { it -> it.digitToInt()}
//        disk.log("disk ")
        var blocks = toDmap(disk)
//        blocks.log("blocks a>")
        if (part == 1) compact1(blocks) else compact2(blocks)
//        blocks.log("blocks b>")
        return checksum(blocks)
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 1928)
    verify("Real part 1", part1(readInput("Day${day}")), 6_366_665_108_136)
    verify("Test part 2", part1(readInput("Day${day}_test"), part=2) , 2858)
    println("Get a coffee, this takes a minute...")
    verify("Real part 2", part1(readInput("Day${day}"), part=2), 6_398_065_450_842)
}
