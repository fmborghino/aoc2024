import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("data/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
fun Any?.log() = println(this)

/**
 * Generate Pair<Int, Int> for range N x N where N is 0 to (n-1)
 * Handy for generating keys for a square grid, say.
 * `generatePairs(2).joinToString() // (0, 0), (0, 1), (1, 0), (1, 1)`
 */
fun generatePairs(n: Int) = sequence {
    (0 until n).forEach { x ->
        (0 until n).forEach { y ->
            yield(Pair(x, y))
        }
    }
}

/**
 * The cleaner shorthand for printing output.
 */
val COLOR_GREEN = "\u001B[0;32m"
val COLOR_RED = "\u001B[0;31m"
val COLOR_YELLOW = "\u001B[0;33m"
val COLOR_RESET = "\u001B[0m"
fun verify(msg: String, actual: Int, expected: Int):Unit {
    verify(msg, actual.toLong(), expected.toLong())
}

fun verify(msg: String, actual: Long, expected: Long):Unit {
    val output = if (actual == expected) "$msg\n    ${COLOR_GREEN}$actual == $expected — OK ${COLOR_RESET}"
    else "$msg\n    ${COLOR_RED}$actual != $expected — FAIL ${COLOR_RESET}"
    println(output)
}

// handy if you want to print the result of an expression AND pass that result back into your call site
// use like: tee { expression }
fun <T> tee(expression: () -> T): T {
    val result = expression()
    println("${COLOR_YELLOW}$result${COLOR_RESET}")
    return result
}
