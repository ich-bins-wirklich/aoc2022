package day8

import util.loadInput
import java.util.stream.IntStream

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/8/input")
    Main(input).run2()
}

class Board(
    val width: Int,
    val height: Int,
    val trees: ArrayList<List<Int>>
) {
    fun invisible(x: Int, y: Int) : Boolean {
        val treeHeight = trees[y][x]

        return IntStream.range(0, x).anyMatch { trees[y][it] >= treeHeight } &&
            IntStream.range(x + 1, width).anyMatch { trees[y][it] >= treeHeight } &&
            IntStream.range(0, y).anyMatch { trees[it][x] >= treeHeight } &&
            IntStream.range(y + 1, height).anyMatch { trees[it][x] >= treeHeight }
    }

    fun scenicScore(x: Int, y: Int): Int {
        return scenicScoreLeft(x, y) * scenicScoreUp(x, y) * scenicScoreRight(x, y) * scenicScoreDown(x, y)
    }

    fun scenicScoreLeft(x: Int, y: Int): Int {
        val treeHeight = trees[y][x]

        for(i in 1 .. x) {
            val currentX = x - i
            if(trees[y][currentX] >= treeHeight) return i
        }

        return x
    }

    fun scenicScoreRight(x: Int, y: Int): Int {
        val treeHeight = trees[y][x]

        for(i in 1 until width - x) {
            val currentX = x + i
            if(trees[y][currentX] >= treeHeight) return i
        }

        return width - x - 1
    }

    fun scenicScoreUp(x: Int, y: Int): Int {
        val treeHeight = trees[y][x]

        for(i in 1 .. y) {
            val currentY = y - i
            if(trees[currentY][x] >= treeHeight) return i
        }

        return y
    }

    fun scenicScoreDown(x: Int, y: Int): Int {
        val treeHeight = trees[y][x]

        for(i in 1 until height - y) {
            val currentY = y + i
            if(trees[currentY][x] >= treeHeight) return i
        }

        return height - y - 1
    }
}

class Main(
    val input: String
) {
    fun run2() {
        val board = parseInput()
        val scores = ArrayList<Int>()

        IntStream.range(0, board.width).forEach {x ->
             IntStream.range(0, board.height).forEach {y ->
                 scores.add(board.scenicScore(x, y))
             }
        }
        val max = scores.max()
        println(max)
    }

    fun run() {
        val board = parseInput()
        val sum = IntStream.range(0, board.width).toArray().fold(0) { agg, x ->
            IntStream.range(0, board.height).toArray().fold(agg) { innerAgg, y ->
                if(board.invisible(x, y)) innerAgg else innerAgg + 1
            }
        }
        println(sum)
    }

    fun parseInput(): Board {
        val lines = input.lines().filter { it != "" }
        val width = lines[0].length
        val height = lines.size

        val trees = arrayListOf<List<Int>>()

        lines.forEach {
            val list = it.map {digit -> digit.digitToInt() }
                .toList()
            trees.add(list)
        }

        return Board(width, height, trees)
    }
}