package day9

import util.loadInput
import java.util.*
import java.util.stream.IntStream
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.abs

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/9/input")
    Main(input).run2()
}

class Point(
    val x: Int,
    val y: Int
) {
    operator fun plus(value: Point): Point {
        return Point(x + value.x, y + value.y)
    }

    override fun equals(other: Any?): Boolean {
        if(other == null || other !is Point) return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }
}

class Main(
    val input: String
) {
    companion object {
        const val SIZE = 10
    }

    fun run2() {
        val segments = Array(SIZE) { Point(0, 0) }
        val visitedPositions = ArrayList<Point>()
        visitedPositions.add(segments.last())

        input.lines()
            .filter { it != "" }
            .forEach {
                val parts = it.split(" ")
                val direction = parts[0].first()
                val steps = parts[1].toInt()

                for(i in 0 until steps) {
                    segments[0] = getUpdatedPositionHead(segments[0], direction)

                    for(j in 1 until segments.size) {
                        segments[j] = getUpdatedPositionTail(segments[j - 1], segments[j])
                    }
                    visitedPositions.add(segments.last())
                }
            }
        println(visitedPositions.distinct().size)
    }

    fun run() {
        var head = Point(0, 0)
        var tail = head
        val visitedPositions = ArrayList<Point>()
        visitedPositions.add(tail)

        input.lines()
            .filter { it != "" }
            .forEach {
                val parts = it.split(" ")
                val direction = parts[0].first()
                val steps = parts[1].toInt()

                for(i in 0 until steps) {
                    head = getUpdatedPositionHead(head, direction)
                    tail = getUpdatedPositionTail(head, tail)
                    visitedPositions.add(tail)
                }
            }
        println(visitedPositions.distinct().size)
    }

    fun getUpdatedPositionHead(head: Point, direction: Char): Point {
        return when(direction) {
            'U' -> head + Point(0, -1)
            'D' -> head + Point(0, 1)
            'L' -> head + Point(-1, 0)
            'R' -> head + Point(1, 0)
            else -> head
        }
    }

    fun getUpdatedPositionTail(head: Point, tail: Point): Point {
        return if(head.x != tail.x && head.y != tail.y) {
            if(abs(head.x - tail.x) > 1 || abs(head.y - tail.y) > 1) {
                val updatedY = tail.y + head.y.compareTo(tail.y)
                val updatedX = tail.x + head.x.compareTo(tail.x)

                Point(updatedX, updatedY)
            } else {
                tail
            }
        } else {
            val updatedY = tail.y + compareTo(head.y, tail.y)
            val updatedX = tail.x + compareTo(head.x, tail.x)

            Point(updatedX, updatedY)
        }
    }

    fun compareTo(x1: Int, x2: Int): Int {
        return if(x1 + 1 < x2) -1
            else if(x1 - 1 > x2) 1
            else 0
    }
}