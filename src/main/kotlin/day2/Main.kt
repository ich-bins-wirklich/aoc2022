package day2

import util.loadInput
import java.lang.IllegalArgumentException

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/2/input")
    Main2(input).run()
}

class Main2(val input: String) {
    fun run() {
        val sum = input
            .split("\n")
            .map {
                if (it == "") {
                    0
                } else {
                    val inputLine = it.split(" ")
                    val theirMove: Char = inputLine[0].first()
                    val result: Char = inputLine[1].first()
                    val score = getResultScore(result) + getMoveScore(getOurMove(theirMove, result))
                    score
                }
            }.sum()
        println(sum)
    }

    fun getOurMove(theirMove: Char, result: Char): Char {
        // -1, 0, 1
        val offset = result - 'Y'
        return (Math.floorMod((theirMove.code - 'A'.code) + offset, 3) + 'X'.code).toChar()
    }

    fun getResultScore(result: Char): Int {
        return (result - 'X') * 3
    }

    fun getMoveScore(move: Char): Int {
        return move - 'X' + 1
    }
}

class Main(val input: String) {

    fun run() {
        val sum = input
            .split("\n")
            .map {
                if (it == "") {
                    0
                } else {
                    val moves = it.split(" ")
                    val theirMove: Char = moves[0].first()
                    val ourMove: Char = moves[1].first()
                    calculateMoveResult(ourMove, theirMove) + getMoveScore(ourMove)
                }
            }.sum()
        println(sum)
    }

    fun calculateMoveResult(ourMove: Char, theirMove: Char): Int {
        val theirMove = mapOpponentMove(theirMove)
        var delta = (ourMove - theirMove)
        delta = Math.floorMod(delta + 1, 3)
        return delta * 3
    }

    fun mapOpponentMove(theirMove: Char): Char {
        return when (theirMove) {
            'A' -> 'X'
            'B' -> 'Y'
            'C' -> 'Z'
            else -> throw IllegalArgumentException(theirMove.toString())
        }
    }

    fun getMoveScore(move: Char): Int {
        return move - 'X' + 1
    }
}