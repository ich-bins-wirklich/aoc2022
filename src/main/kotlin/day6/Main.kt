package day6

import util.loadInput

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/6/input")
    Main(input).run()
}

class Main(
    val input: String
) {
    val size = 14

    fun run() {
        input.forEachIndexed { index, _ ->
            if(index > size) {
                val lastFourSymbols = input.substring(index - size + 1, index + 1)
                if(lastFourSymbols.chars().distinct().count() == size.toLong()) {
                    println(index + 1)
                    return
                }
            }
        }
    }

}