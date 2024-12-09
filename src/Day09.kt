import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.block

typealias Id = Int
const val FREE: Id = -1

fun main() {
    val day="09"
    fun log(message: Any?) {
        println(message)
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

    fun findFirstFast(list: List<Int>, start: Int, target: Int): Int {
        if (start !in list.indices) return -1
        for (i in start until list.size) {
            if (list[i] == target) return i
        }
        return -1
    }

    fun compact(blocks: MutableList<Int>) {
        var lastFree = findFirstFast(blocks, 0, FREE)
        for (i in blocks.size - 1 downTo 0) {
            val moveBlock = blocks[i]
            val nextFree = findFirstFast(blocks, lastFree, FREE)
            if (nextFree > i) return // our next FREE block is in the end-zone

            if (blocks[i] != FREE) {
                blocks[nextFree] = moveBlock
                blocks[i] = FREE
//                log("moved $nextFree <-> $i ")
            }
        }
    }


    fun checksum(blocks: List<Id>): Long {
        return blocks.map { it.toLong() }.withIndex().takeWhile { it.value != -1L }.sumOf { it.value * it.index  }.toLong()
    }

    fun part1(input: List<String>): Long {
//        input.log("input ")
        var disk = input[0].map { it -> it.digitToInt()}
//        disk.log("disk ")
        var blocks = toDmap(disk)
//        blocks.log("blocks a>")
        compact(blocks)
//        blocks.log("blocks b>")
        return checksum(blocks)
    }

    fun part2(input: List<String>): Int {
        return 2 * input.sumOf { it.toInt() }
    }

    verify("Test part 1", part1(readInput("Day${day}_test")), 1928)
    verify("Real part 1", part1(readInput("Day${day}")), 6_366_665_108_136)
//    verify("Test part 2", part2(readInput("Day${day}_test")) , 12)
//    verify("Real part 2", part2(readInput("Day${day}")), 1200)
}
