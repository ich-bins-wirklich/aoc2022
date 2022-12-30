package day15

import util.loadInput
import kotlin.math.abs
import kotlin.math.max

fun main() {
    var input = loadInput("https://adventofcode.com/2022/day/15/input")
    Main(input).run2()
}

class Point(
    val x: Int,
    val y: Int
) {
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

    fun distance(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }
}

class Main(
    val input: String
) {

    fun run2() {
        val regex = getRegex()
        val sensorBeaconPairs = mutableListOf<Pair<Point, Point>>()

        input.lines()
            .filter { it != "" }
            .forEach {line ->
                val result = regex.matchEntire(line)
                val values = result!!.groupValues
                val sensorPosition = Point(values[1].toInt(), values[2].toInt())
                val beaconPosition = Point(values[3].toInt(), values[4].toInt())

                sensorBeaconPairs.add(Pair(sensorPosition, beaconPosition))
            }

        val size = 4_000_000L
        for (y in 0..size) {
            val checkedIntervalsSorted = sensorBeaconPairs
                .filter { projectedWidth(it, y.toInt()) > 0 }
                .map {
                    val width = projectedWidth(it, y.toInt())
                    val sensorX = it.first.x
                    Pair(sensorX - width / 2, sensorX + width / 2)
                }.sortedBy { it.first }

            var firstUncheckedX = 0
            val iterator = checkedIntervalsSorted.iterator()
            while(iterator.hasNext()) {
                val interval = iterator.next()

                if(firstUncheckedX < interval.first) {
                    // case: found solution
                    val x = firstUncheckedX
                    val solution = x * size + y
                    println(solution)
                    return
                }

                firstUncheckedX = max(firstUncheckedX, interval.second + 1)
            }

            if(firstUncheckedX <= size) {
                // case: found solution
                val x = firstUncheckedX
                val solution = x * size + y
                println(solution)
                return
            }
        }
    }

    fun projectedWidth(sensorBeaconPair: Pair<Point, Point>, currentY: Int): Int {
        val sensorPosition = sensorBeaconPair.first
        val beaconPosition = sensorBeaconPair.second

        val distanceToBeacon = sensorPosition.distance(beaconPosition)
        val distanceToTargetLine = abs(currentY - sensorPosition.y)

        val projectedWidth = 2 * (distanceToBeacon - distanceToTargetLine) + 1
        return if(projectedWidth < 0) 0 else projectedWidth
    }

    fun run() {
        val pointSet = HashSet<Point>()
        val regex = getRegex()
        val targetY = 2_000_000
        val beaconsOnTargetLine = HashSet<Point>()

        input.lines()
            .filter { it != "" }
            .forEach {line ->
                val result = regex.matchEntire(line)
                val values = result!!.groupValues
                val sensorPosition = Point(values[1].toInt(), values[2].toInt())
                val beaconPosition = Point(values[3].toInt(), values[4].toInt())
                if(beaconPosition.y == targetY) beaconsOnTargetLine.add(beaconPosition)

                val distanceToBeacon = sensorPosition.distance(beaconPosition)
                val distanceToTargetLine = abs(targetY - sensorPosition.y)

                val projectedWidth = 2 * (distanceToBeacon - distanceToTargetLine) + 1
                val startX = sensorPosition.x - projectedWidth / 2
                val endX = startX + projectedWidth

                for(x in startX until endX) pointSet.add(Point(x, targetY))
            }

        println(pointSet.size - beaconsOnTargetLine.size)
    }

    fun getRegex(): Regex {
        return Regex("Sensor at x=(-?[\\d]+), y=(-?[\\d]+): closest beacon is at x=(-?[\\d]+), y=(-?[\\d]+)")
    }
}