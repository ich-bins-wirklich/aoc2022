package day4

import util.loadInput

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/4/input")
    Main2(input).run()
}

class Main2(val input: String) {

    fun run() {
        val count = input.split("\n")
            .filter { it != "" }
            .count {
                val parts = it.split(",")
                val left = parts[0].split("-").map { Integer.parseInt(it) }
                val right = parts[1].split("-").map { Integer.parseInt(it) }

                !(left[1] < right[0] || left[0] > right[1])
            }
        println(count)
    }
}

class Main(val input: String) {

    fun run() {
        val count = input.split("\n")
            .filter { it != "" }
            .count {
                val parts = it.split(",")
                val left = parts[0].split("-").map { Integer.parseInt(it) }
                val right = parts[1].split("-").map { Integer.parseInt(it) }

                val containment = left[0] <= right[0] && left[1] >= right[1]
                        || left[0] >= right[0] && left[1] <= right[1]
                containment
            }
        println(count)
    }
}