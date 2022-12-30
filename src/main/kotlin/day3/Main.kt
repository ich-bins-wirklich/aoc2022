package day3

import util.loadInput
import java.util.stream.IntStream

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/3/input")
    Main(input).run()
}

class Main2(val input: String) {
    fun run() {

    }
}

class Main(val input: String) {

    fun run() {
        val rucksacks = input
            .split("\n")
            .filter { it != "" }
        val sum = IntStream.range(0, rucksacks.size / 3)
            .map {
                convertToPriority(findBadgeItem(arrayOf(rucksacks[3 * it], rucksacks[3 * it + 1], rucksacks[3 * it + 2])))
            }.sum()
        println(sum)
    }

    fun findBadgeItem(rucksacks: Array<String>): Char {
        return rucksacks.fold(rucksacks[0]) { left, right ->
            String(left.toCharArray().toSet().intersect(right.toCharArray().toSet()).toCharArray())
        }[0]
    }

    fun convertToPriority(item: Char): Int {
        if (item in 'a'..'z') return item - 'a' + 1
        return item - 'A' + 27
    }
}