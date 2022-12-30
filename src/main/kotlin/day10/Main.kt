package day10

import util.loadInput
import java.util.*
import java.util.stream.IntStream
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.abs

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/10/input")
    Main(input).run2()
}

class Main(
    val input: String
) {
    companion object {
        const val WIDTH = 40
        const val HEIGHT = 6
    }

    fun run2() {
        val registerValues = ArrayList<Int>()
        registerValues.add(1)

        input.lines()
            .filter { it != "" }
            .forEach {
                registerValues.add(registerValues.last())
                if(it.startsWith("addx")) {
                    val value = it.split(" ")[1].toInt()
                    registerValues.add(registerValues.last() + value)
                }
            }

        for(y in 0 until HEIGHT) {
            for(x in 0 until WIDTH) {
                val index = WIDTH * y + x
                val registerValue = registerValues[index]
                val outputChar = if(abs(x - registerValue) <= 1) '#' else '.'
                print(outputChar)
            }
            println()
        }
    }

    fun run() {
        val registerValues = ArrayList<Int>()
        registerValues.add(1)

        input.lines()
            .filter { it != "" }
            .forEach {
                registerValues.add(registerValues.last())
                if(it.startsWith("addx")) {
                    val value = it.split(" ")[1].toInt()
                    registerValues.add(registerValues.last() + value)
                }
            }

        var sum = 0
        for (i in 19 until registerValues.size step 40) {
            sum += (i + 1) * registerValues[i]
        }
        println(sum)
    }
}