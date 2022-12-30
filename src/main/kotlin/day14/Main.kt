package day14

import util.loadInput
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    var input = loadInput("https://adventofcode.com/2022/day/14/input")
    Main(input).run2()
}

class Point(
    val x: Int,
    val y: Int
) {
    operator fun plus(otherPoint: Point): Point {
        return Point(x + otherPoint.x, y + otherPoint.y)
    }
}

class Main(
    val input: String
) {

    fun run2() {
        val board = parseInput2()
        val height = board.size

        var droppedSand = 0
        var currentSandPosition = Point(500, 0)
        while(!board[0][500]) {
            if(!board[currentSandPosition.y + 1][currentSandPosition.x]) {
                // case: fall straight down
                currentSandPosition += Point(0, 1)
            } else if(!board[currentSandPosition.y + 1][currentSandPosition.x - 1]) {
                // case: fall down left
                currentSandPosition += Point(-1, 1)
            } else if(!board[currentSandPosition.y + 1][currentSandPosition.x + 1]) {
                // case: fall down right
                currentSandPosition += Point(1, 1)
            } else {
                // case: land
                droppedSand++
                board[currentSandPosition.y][currentSandPosition.x] = true
                currentSandPosition = Point(500, 0)
            }
        }

        println(droppedSand)
    }

    fun run() {
        val board = parseInput()
        val height = board.size

        var droppedSand = 0
        var currentSandPosition = Point(500, 0)
        while(currentSandPosition.y + 1 != height) {
            if(!board[currentSandPosition.y + 1][currentSandPosition.x]) {
                // case: fall straight down
                currentSandPosition += Point(0, 1)
            } else if(!board[currentSandPosition.y + 1][currentSandPosition.x - 1]) {
                // case: fall down left
                currentSandPosition += Point(-1, 1)
            } else if(!board[currentSandPosition.y + 1][currentSandPosition.x + 1]) {
                // case: fall down right
                currentSandPosition += Point(1, 1)
            } else {
                // case: land
                droppedSand++
                board[currentSandPosition.y][currentSandPosition.x] = true
                currentSandPosition = Point(500, 0)
            }
        }

        println(droppedSand)
    }

    fun parseInput2(): Array<BooleanArray> {
        val lineList = mutableListOf<Pair<Point, Point>>()

        input.lines()
            .filter { it != "" }
            .forEach { line ->
                val parts = line.split(" -> ")
                for(i in 1 until parts.size) {
                    val firstPoint = parsePoint(parts[i - 1])
                    val secondPoint = parsePoint(parts[i])
                    val line = Pair(firstPoint, secondPoint)
                    lineList.add(line)
                }
            }

        val maxX = lineList.maxOf { max(it.first.x, it.second.x) } * 2
        val maxY = lineList.maxOf { max(it.first.y, it.second.y) } + 2

        val board = Array(maxY + 1) { BooleanArray(maxX + 1) { false } }
        lineList.forEach { processLine(board, it) }
        val groundLine = Pair(Point(0, maxY), Point(maxX, maxY))
        processLine(board, groundLine)

        return board
    }

    fun parseInput(): Array<BooleanArray> {
        val lineList = mutableListOf<Pair<Point, Point>>()

        input.lines()
            .filter { it != "" }
            .forEach { line ->
                val parts = line.split(" -> ")
                for(i in 1 until parts.size) {
                    val firstPoint = parsePoint(parts[i - 1])
                    val secondPoint = parsePoint(parts[i])
                    val line = Pair(firstPoint, secondPoint)
                    lineList.add(line)
                }
            }

        val maxX = lineList.maxOf { max(it.first.x, it.second.x) } + 1
        val maxY = lineList.maxOf { max(it.first.y, it.second.y) } + 1

        val board = Array(maxY + 1) { BooleanArray(maxX + 1) { false } }
        lineList.forEach { processLine(board, it) }

        return board
    }

    fun processLine(board: Array<BooleanArray>, line: Pair<Point, Point>) {
        val first = line.first
        val second = line.second

        if(first.x == second.x) {
            for(y in min(first.y, second.y) .. max(first.y, second.y)) {
                board[y][first.x] = true
            }
        } else {
            for(x in min(first.x, second.x) .. max(first.x, second.x)) {
                board[first.y][x] = true
            }
        }
    }

    fun parsePoint(coordinates: String): Point {
        val parts = coordinates.split(",")
        return Point(parts[0].toInt(), parts[1].toInt())
    }
}