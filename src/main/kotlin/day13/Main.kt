package day13

import util.loadInput
import java.lang.Integer.min

fun main() {
    var input = loadInput("https://adventofcode.com/2022/day/13/input")
    Main(input).run2()
}

open class Element

class Number(
    val value: Int
): Element() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Number

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }

    override fun toString(): String {
        return value.toString()
    }
}

class ElementList(
    val contents: List<Element>
): Element() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ElementList

        if (contents != other.contents) return false

        return true
    }

    override fun hashCode(): Int {
        return contents.hashCode()
    }

    override fun toString(): String {
        val contentString = contents.map { it.toString() }.joinToString(",")
        return "[$contentString]"
    }
}

class Main(
    val input: String
) {

    fun run2() {
        val inputList = parseInput().toMutableList()
        val dividers = listOf(
            ElementList(listOf(ElementList(listOf(Number(2))))),
            ElementList(listOf(ElementList(listOf(Number(6)))))
        )
        inputList.addAll(dividers)

        val sortedList = inputList.sortedWith { left, right ->
            areRightOrder(left, right)
        }.reversed()

        val dividerIndices = mutableListOf<Int>()
        sortedList.forEachIndexed { index, element ->
            println(element)
            if(dividers.any { it == element }) dividerIndices.add(index)
        }

        println(dividerIndices.fold(1) { agg, value -> agg * (value + 1) })
    }

    fun run() {
        val inputList = parseInput()
        var indexSum = 0

        for(i in 0 until inputList.size / 2) {
            val left = inputList[2 * i]
            val right = inputList[2 * i + 1]
            val rightOrder = areRightOrder(left, right) >= 0

            if(rightOrder) indexSum += i + 1
        }

        println(indexSum)
    }

    fun areRightOrder(left: Element, right: Element): Int {
        if(left is Number && right is Number) {
            return compareValues(right.value, left.value)
        } else if(left is ElementList && right is ElementList) {
            for(i in 0 until min(left.contents.size, right.contents.size)) {
                val compareResult = areRightOrder(left.contents[i], right.contents[i])
                if(compareResult != 0) return compareResult
            }

            return compareValues(right.contents.size, left.contents.size)
        } else {
            return if(left is Number) areRightOrder(ElementList(listOf(left)), right)
            else areRightOrder(left, ElementList(listOf(right)))
        }
    }

    fun parseInput(): List<Element> {
        return input.lines()
            .filter { it != "" }
            .map { parseLine(it) }
            .toList()
    }

    fun parseLine(line: String): Element {
        return if(line.startsWith("[")){
            // case: list
            val trimmedLine = line.removePrefix("[").removeSuffix("]")
            val listContents = splitOuter(trimmedLine, ',')
            val parsedListContents = listContents
                .map { parseLine(it) }
                .toList()
            ElementList(parsedListContents)
        } else if(line.isEmpty()) {
            // case: empty list
            ElementList(listOf())
        } else {
            // case: number
            Number(line.toInt())
        }
    }

    fun splitOuter(line: String, delimiter: Char): List<String> {
        val splitList = mutableListOf<String>()
        var startIndex = 0
        var bracketLevel = 0

        for(currentIndex in line.indices) {
            val currentChar = line[currentIndex]

            when(currentChar) {
                '[' -> bracketLevel++
                ']' -> bracketLevel--
                delimiter -> {
                    if(bracketLevel != 0) continue
                    val splitPart = line.substring(startIndex, currentIndex)
                    splitList.add(splitPart)
                    startIndex = currentIndex + 1
                }
            }
        }

        splitList.add(line.substring(startIndex, line.length))
        return splitList.toList()
    }

}