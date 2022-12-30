package day17

import util.loadInput
import java.lang.Integer.max
import java.math.BigInteger

fun main() {
    var input = loadInput("https://adventofcode.com/2022/day/17/input")
//    input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
    input = input.filter { it == '<' || it == '>' }
    Main(input).run()
}

class Point(
    val x: Int,
    val y: Int
) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    fun isBlocked(board: MutableList<BooleanArray>): Boolean {
        return board[y][x]
    }

    fun block(board: MutableList<BooleanArray>) {
        board[y][x] = true
    }
}

class Rock(
    val width: Int,
    val height: Int,
    val points: Array<Point>
)

class Main(
    val input: String
) {
    companion object {
        const val WIDTH = 7
        val ROCKS = listOf(
            // ####
            Rock(4, 1, arrayOf(
                    Point(0, 0),
                    Point(1, 0),
                    Point(2, 0),
                    Point(3, 0)
                )
            ),
//            .#.
//            ###
//            .#.
            Rock(3, 3, arrayOf(
                Point(0, 1),
                Point(1, 1),
                Point(2, 1),
                Point(1, 0),
                Point(1, 2)
                )
            ),
//            ..#
//            ..#
//            ###
            Rock(3, 3, arrayOf(
                Point(0, 0),
                Point(1, 0),
                Point(2, 0),
                Point(2, 1),
                Point(2, 2)
                )
            ),
//            #
//            #
//            #
//            #
            Rock(1, 4, arrayOf(
                Point(0, 0),
                Point(0, 1),
                Point(0, 2),
                Point(0, 3)
                )
            ),
//            ##
//            ##
            Rock(2, 2, arrayOf(
                Point(0, 0),
                Point(0, 1),
                Point(1, 0),
                Point(1, 1)
                )
            ),
        )
        const val MAX_ROCKS = 1_000_000_000_000
    }

    fun run()  {
        val board = mutableListOf<BooleanArray>()
        var highestRock = -1
        var directions = input.toCharArray().iterator()

        var stage = 0
        var towerHeight = BigInteger.ZERO
        var highestRockStage0 = 0
        var highestRockStage1 = 0
        var rocksLeftAfterStage0 = 0L

        var offsetAfterStage1 = 0L

        outer@ for (i in 0 until MAX_ROCKS) {
            if(stage == 2 && i + offsetAfterStage1 == MAX_ROCKS) {
                val heightDelta = highestRock - highestRockStage1
                towerHeight += heightDelta.toBigInteger()
                break
            }

            if(!directions.hasNext()) {
                if(stage == 0) {
                    towerHeight = (highestRock + 1).toBigInteger()
                    highestRockStage0 = highestRock
                    rocksLeftAfterStage0 = MAX_ROCKS - i
                    stage++
                    println("finished stage 0")
                } else if(stage == 1) {
                    highestRockStage1 = highestRock
                    val rocksLeftAfterStage1 = MAX_ROCKS - i
                    // amount of rocks processed after one full stage 1 cycle
                    val rockDelta = rocksLeftAfterStage0 - rocksLeftAfterStage1
                    // amount of height gained after one full stage 1 cycle
                    val heightDelta = highestRock - highestRockStage0
                    val stage1Cycles = rocksLeftAfterStage0 / rockDelta
                    towerHeight += stage1Cycles.toBigInteger().multiply(heightDelta.toBigInteger())

                    offsetAfterStage1 = rockDelta * (stage1Cycles - 1)
                    stage++
                    println("finished stage 1")
                }
            }

            var rockPosition = spawnNewRock(highestRock)
            val rock = ROCKS[(i % ROCKS.size).toInt()]

            val boardHeight = rockPosition.y + rock.height
            ensureBoardSize(board, boardHeight)
            if(!directions.hasNext()) directions = input.iterator()
            rockPosition = jetMoveRockPosition(rock, rockPosition, directions.next())

            while(true) {
                var newRockPosition = rockPosition + Point(0, -1)

                if(rockHits(rock, newRockPosition, board)) {
                    // case: rock hits ground/other rock, come to a halt
                    val upperY = rockPosition.y + rock.height - 1
                    highestRock = max(highestRock, upperY)
                    rock.points
                        .map { it + rockPosition }
                        .forEach { it.block(board) }

                    continue@outer
                }

                if(!directions.hasNext()) directions = input.iterator()
                rockPosition = newRockPosition
                newRockPosition = jetMoveRockPosition(rock, rockPosition, directions.next())
                if(!rockHits(rock, newRockPosition, board)) rockPosition = newRockPosition
            }
        }

        println(towerHeight)
//        printBoard(board)
    }

    fun printBoard(board: MutableList<BooleanArray>) {
        board.reversed().forEach {
            it.forEach { rock -> if(rock) print('#') else print('.') }
            println()
        }
    }

    fun rockHits(rock: Rock, rockPosition: Point, board: MutableList<BooleanArray>): Boolean {
        if(rockPosition.y == -1) return true
        return rock.points
            .map { it + rockPosition }
            .any { it.isBlocked(board) }
    }

    fun ensureBoardSize(board: MutableList<BooleanArray>, size: Int) {
        while(board.size < size) {
            board.add(BooleanArray(WIDTH) { false })
        }
    }

    fun spawnNewRock(highestRock: Int): Point {
        return Point(2, highestRock + 4)
    }

    fun jetMoveRockPosition(rock: Rock, rockPosition: Point, direction: Char): Point {
        var newRockPosition = rockPosition
        return when(direction) {
            '<' -> {
                if(rockPosition.x != 0) newRockPosition += Point(-1, 0)
                newRockPosition
            }
            '>' -> {
                if(rockPosition.x + rock.width != WIDTH) newRockPosition += Point(1, 0)
                newRockPosition
            }
            else -> newRockPosition
        }
    }
}