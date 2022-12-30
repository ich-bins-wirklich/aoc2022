package day20

import util.loadInput
import java.lang.Integer.max

fun main() {
    var input = loadInput("https://adventofcode.com/2022/day/20/input")
//    input = "1\n" +
//            "2\n" +
//            "-3\n" +
//            "3\n" +
//            "-2\n" +
//            "0\n" +
//            "4"
    Main(input).run()
}

class Element(
    val value: Int,
    var index: Int
)

class Main(
    val input: String
) {

    fun run() {
        val originalElementList = parseInput()
        val workingElementList = originalElementList.toMutableList()

        val size = originalElementList.size
        originalElementList.forEach { element ->
            val index = element.index
            var targetIndex = element.index + element.value

            if(index != targetIndex) {
                if(element.value < 0) targetIndex--

                var currentIndex = index + 1

                while(Math.floorMod(currentIndex, size) != Math.floorMod(targetIndex + 1, size)) {
                    val lastIndexNormalized = Math.floorMod(currentIndex - 1, size)
                    val currentIndexNormalized = Math.floorMod(currentIndex, size)

                    val lastElement = workingElementList[lastIndexNormalized]
                    val currentElement = workingElementList[currentIndexNormalized]

                    workingElementList[currentIndexNormalized] = lastElement
                    workingElementList[lastIndexNormalized] = currentElement

                    currentElement.index--
                    lastElement.index++

                    currentIndex++
                }
            }
        }

//    fun run() {
//        val originalElementList = parseInput()
//        val workingElementList = originalElementList.toMutableList()
//
//        val size = originalElementList.size
//        originalElementList.forEach { element ->
//            val index = element.index
//            var targetIndex = element.index + element.value
//
//            if(index != targetIndex) {
//                var direction = if(Math.floorMod(index, size) < Math.floorMod(targetIndex, size)) 1 else -1
//                if(targetIndex % size == 0) direction = 1
//                if(direction == -1) targetIndex -= direction
//                if(element.value < 0) targetIndex--
//
//                var currentIndex = index + direction
//
//                while(Math.floorMod(currentIndex, size) != Math.floorMod(targetIndex + direction, size)) {
//                    val lastIndexNormalized = Math.floorMod(currentIndex - direction, size)
//                    val currentIndexNormalized = Math.floorMod(currentIndex, size)
//
//                    val lastElement = workingElementList[lastIndexNormalized]
//                    val currentElement = workingElementList[currentIndexNormalized]
//
//                    workingElementList[currentIndexNormalized] = lastElement
//                    workingElementList[lastIndexNormalized] = currentElement
//
//                    currentElement.index -= direction
//                    lastElement.index += direction
//
//                    currentIndex += direction
//                }
//            }
//        }

//        println(workingElementList.joinToString(",") { it.value.toString() })

        val result = arrayOf(1000, 2000, 3000).sumOf {
            val result = getAfterZero(workingElementList, it)
            println(result)
            result
        }
        println(result)
    }

    fun getAfterZero(list: MutableList<Element>, index: Int): Int {
        val zeroElement = list.find { it.value == 0 }!!
        val targetElement = list[Math.floorMod(zeroElement.index + index, list.size)]
        return targetElement.value
    }

    fun parseInput(): List<Element> {
//        println(input.lines().filter { it != "" }.distinct().count())
//        println(input.lines().filter { it == "0" }.count())
//        println()

        return input.lines()
            .filter { it != "" }
            .mapIndexed { index, value ->
                Element(value.toInt(), index)
            }
    }

}