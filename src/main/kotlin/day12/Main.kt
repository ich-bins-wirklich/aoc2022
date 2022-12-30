package day12

import util.loadInput

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/12/input")
    Main(input).run()
}

class Board(
    val data: List<List<Int>>,
    val width: Int,
    val height: Int,
    val startPosition: Point,
    val endPosition: Point
)

class Point(
    val x: Int,
    val y: Int
) {
    fun height(board: Board): Int {
        return board.data[y][x]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

class Main(
    val input: String
) {

    fun run() {
        val board = parseInput()
        var visitedMapOld = Array(board.height) { BooleanArray(board.width) }
//        visitedMapOld[board.startPosition.y][board.startPosition.x] = true

        for(y in 0 until board.height) {
            for(x in 0 until board.width) {
                val position = Point(x, y)
                if(position.height(board) == 0) visitedMapOld[y][x] = true
            }
        }

        for(steps in 1 until board.width * board.height) {
            val visitedMapNew = visitedMapOld.map { it.copyOf() }.toTypedArray()

            for(y in 0 until board.height) {
                for(x in 0 until board.width) {
                    if(visitedMapNew[y][x]) continue
                    val position = Point(x, y)

                    val adjacentPoints = mutableListOf<Point>()
                    if(x != 0) adjacentPoints.add(Point(x - 1, y))
                    if(x != board.width - 1) adjacentPoints.add(Point(x + 1, y))
                    if(y != 0) adjacentPoints.add(Point(x, y - 1))
                    if(y != board.height - 1) adjacentPoints.add(Point(x, y + 1))

                    val canBeReached = adjacentPoints
                        .filter { visitedMapOld[it.y][it.x] }
                        .any { canReachCell(board, it, position) }
                    if(canBeReached) {
                        visitedMapNew[y][x] = true

                        if(position == board.endPosition) {
                            println(steps)
                            return
                        }
                    }
                }
            }

            visitedMapOld = visitedMapNew
        }
    }

    fun canReachCell(board: Board, from: Point, to: Point): Boolean {
        val fromHeight = from.height(board)
        val toHeight = to.height(board)

        return toHeight - fromHeight <= 1
    }

    fun parseInput(): Board {
        val data = input.lines()
            .filter { it != "" }
            .map { line ->
                line.toCharArray().map {
                    when(it) {
                        'S' -> 'a'
                        'E' -> 'z'
                        else -> it
                    }
                }.map { char ->
                    char.code - 'a'.code
                }.toList()
            }.toList()

        val width = data[0].size
        val height = data.size

        var startPosition = Point(0, 0)
        var endPosition = Point(0, 0)
        input.lines()
            .filter { it != "" }
            .forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    if(char == 'S') startPosition = Point(x, y)
                    else if(char == 'E') endPosition = Point(x, y)
                }
            }

        return Board(data, width, height, startPosition, endPosition)
    }
}