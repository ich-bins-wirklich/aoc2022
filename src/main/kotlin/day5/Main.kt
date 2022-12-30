package day5

import util.loadInput

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/5/input")
    Main(input).run2()
}

class Move(
    val amount: Int,
    val from: Int,
    val to: Int
)

class Main(val input: String) {

    fun run2() {
        val crateList = parseCrates()
        val moves = parseMoves()
        executeMoves2(crateList, moves)
    }

    fun run() {
        val crateList = parseCrates()
        val moves = parseMoves()
        executeMoves(crateList, moves)
    }

    fun parseCrates(): List<ArrayDeque<Char>> {
        val lines = input.split("\n").filter { it != "" }
        val width = (lines[0].length - 3) / 4 + 1
        val crateLines = lines.filter { it.contains("[") }

        val list = arrayListOf<ArrayDeque<Char>>()
        for (i in 0 until width) list.add(ArrayDeque())

        val regex = Regex(getCrateRegexString(width))
        crateLines.reversed().forEach { line ->
            regex.findAll(line)
                .first()
                .groupValues
                .filterIndexed { index, _ -> index > 0 }
                .forEachIndexed { index, element ->
                    if(element != " ") {
                        list[index].add(element.first())
                    }
                }
        }

        return list
    }

    fun parseMoves(): List<Move> {
        val regex = Regex(getMoveRegexString())

        return input.split("\n")
            .filter { it.startsWith("move")}
            .map {
                val parsedLine = regex.find(it)!!.groupValues
                Move(parsedLine[1].toInt(), parsedLine[2].toInt() - 1, parsedLine[3].toInt() - 1)
            }.toList()
    }

    fun executeMoves(crateList: List<ArrayDeque<Char>>, moveList: List<Move>) {
        moveList.forEach { moveCrates(crateList, it) }
        crateList.forEach { print(it.last()) }
    }

    fun executeMoves2(crateList: List<ArrayDeque<Char>>, moveList: List<Move>) {
        moveList.forEach { moveCrates2(crateList, it) }
        crateList.forEach { print(it.last()) }
    }

    fun getCrateRegexString(width: Int): String {
        var regexString = ".(.)."
        for (i in 1 until width) {
            regexString += " .(.)."
        }
        return regexString
    }

    fun getMoveRegexString(): String {
        return "move ([0-9]+) from ([0-9]+) to ([0-9]+)"
    }

    fun moveCrates(list: List<ArrayDeque<Char>>, move: Move) {
        for (i in 0 until move.amount) {
            list[move.to].add(list[move.from].removeLast())
        }
    }

    fun moveCrates2(list: List<ArrayDeque<Char>>, move: Move) {
        val tempStack = ArrayDeque<Char>()

        for (i in 0 until move.amount) {
            tempStack.add(list[move.from].removeLast())
        }

        for (i in 0 until move.amount) {
            list[move.to].add(tempStack.removeLast())
        }
    }
}