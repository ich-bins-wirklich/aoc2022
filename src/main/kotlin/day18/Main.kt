package day18

import util.loadInput
import java.lang.Integer.max
import java.math.BigInteger

fun main() {
    var input = loadInput("https://adventofcode.com/2022/day/18/input")
    Main(input).run()
}

class Point(
    val x: Int,
    val y: Int,
    val z: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }
}

class Main(
    val input: String
) {

    fun run() {
        var surfaceSum = 0
        val space = parseInput()
        val outsidePoints = getOutsidePoints(space)
        val insidePoints = mutableListOf<Point>()
        for (x in space.indices) {
            for (y in space[0].indices) {
                for (z in space[0][0].indices) {
                    if(space[x][y][z] || outsidePoints.contains(Point(x, y, z))) continue
                    insidePoints.add(Point(x, y, z))
                }
            }
        }

        for (x in space.indices) {
            for (y in space[0].indices) {
                for (z in space[0][0].indices) {
                    if (!space[x][y][z]) continue
                    surfaceSum += getSurfaceArea(space, outsidePoints, x, y, z)
                }
            }
        }

        println(surfaceSum)
    }

    fun parseInput(): Array<Array<BooleanArray>> {
        val points = input.lines()
            .filter { it != "" }
            .map {
                val parts = it.split(",")
                arrayOf(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
            }
        val maxX = points.maxOf { it[0] }
        val maxY = points.maxOf { it[1] }
        val maxZ = points.maxOf { it[2] }

        val space = Array(maxX + 3) { Array(maxY + 3) { BooleanArray(maxZ + 3) { false } } }
        points.forEach {
            space[it[0] + 1][it[1] + 1][it[2] + 1] = true
        }
        return space
    }

    fun getOutsidePoints(space: Array<Array<BooleanArray>>): Set<Point> {
        val initialPoint = Point(space.size - 1, space[0].size - 1, space[0][0].size - 1)
        val outsidePoints = mutableSetOf(initialPoint)
        val pointQueue = ArrayDeque(listOf(initialPoint))

        while (!pointQueue.isEmpty()) {
            val point = pointQueue.removeFirst()
            val x = point.x
            val y = point.y
            val z = point.z

            val adjacentPoints = arrayOf(
                Point(x - 1, y, z),
                Point(x + 1, y, z),
                Point(x, y - 1, z),
                Point(x, y + 1, z),
                Point(x, y, z - 1),
                Point(x, y, z + 1),
            )
            adjacentPoints.forEach {
                processOutsidePoint(space, outsidePoints, pointQueue, it.x, it.y, it.z)
            }
        }

        return outsidePoints.toSet()
    }

    fun processOutsidePoint(
        space: Array<Array<BooleanArray>>,
        outsidePoints: MutableSet<Point>,
        pointQueue: ArrayDeque<Point>,
        x: Int,
        y: Int,
        z: Int
    ) {
        if (x < 0 || x == space.size || y < 0 || y == space[0].size || z < 0 || z == space[0][0].size) return
        if(space[x][y][z]) return

        val point = Point(x, y, z)
        if(outsidePoints.add(point)) pointQueue.add(point)
    }

    fun getSurfaceArea(space: Array<Array<BooleanArray>>, outsidePoints: Set<Point>, x: Int, y: Int, z: Int): Int {
        val surfaceChecks = booleanArrayOf(
            !space[x - 1][y][z] && outsidePoints.contains(Point(x - 1, y, z)),
            !space[x][y - 1][z] && outsidePoints.contains(Point(x, y - 1, z)),
            !space[x][y][z - 1] && outsidePoints.contains(Point(x, y, z - 1)),
            !space[x + 1][y][z] && outsidePoints.contains(Point(x + 1, y, z)),
            !space[x][y + 1][z] && outsidePoints.contains(Point(x, y + 1, z)),
            !space[x][y][z + 1] && outsidePoints.contains(Point(x, y, z + 1)),
        )
        return surfaceChecks.count { it }
    }
}