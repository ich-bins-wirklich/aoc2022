package day7

import util.loadInput

fun main() {
    val input = loadInput("https://adventofcode.com/2022/day/7/input")
    Main(input).run2()
}

abstract class Node(
    val name: String
) {
    abstract fun print(lineIndentation: String)
    abstract fun size(): Int
}

class Directory(
    name: String,
    val parent: Directory?,
    val contents: ArrayList<Node> = arrayListOf()
) : Node(name) {
    private var cachedSize: Int? = null

    override fun print(lineIndentation: String) {
        println("$lineIndentation$name (dir)")
        contents.forEach {
            it.print("$lineIndentation  ")
        }
    }

    override fun size(): Int {
        if(cachedSize == null) {
            cachedSize = contents.fold(0) { currentSum, node ->
                currentSum + node.size()
             }
        }

        return cachedSize!!
    }

    fun sumRelevantDirectorySizes(): Int {
        var selfSum = if (Main.meetsCondition(size())) size() else 0

        return contents.filter { it is Directory }
            .fold(selfSum) { currentSum, directory ->
                currentSum + (directory as Directory).sumRelevantDirectorySizes()
            }
    }

    fun fillFullDirectoryList(list: ArrayList<Directory>) {
        list.add(this)
        directories().forEach { it.fillFullDirectoryList(list) }
    }

    fun directories(): List<Directory> {
        return contents.filterIsInstance<Directory>().toList()
    }
}

class File(
    name: String,
    val size: Int
) : Node(name) {
    override fun print(lineIndentation: String) {
        println("$lineIndentation$name (file, size=$size)")
    }

    override fun size(): Int {
        return size
    }
}

class Main(
    val input: String
) {
    companion object {
        fun meetsCondition(size: Int): Boolean {
            return size <= 100_000
        }

        const val TOTAL_SPACE = 70_000_000
        const val REQUIRED_SPACE = 30_000_000
    }

    val root = Directory("/", null)
    var currentDirectory = root

    fun run2() {
        parseInput()

        val usedSpace = root.size()
        val spaceToFree = usedSpace + REQUIRED_SPACE - TOTAL_SPACE

        val fullDirectoryList = arrayListOf<Directory>()
        root.fillFullDirectoryList(fullDirectoryList)

        val size = fullDirectoryList
            .map { it.size() }
            .filter { it >= spaceToFree }
            .min()
        println(size)
    }

    fun run() {
        parseInput()
        println(root.sumRelevantDirectorySizes())
    }

    fun parseInput() {
        input.split("\n")
            .filterIndexed { index, element ->
                // assume first line == '$ cd /'
                index > 0 && element != ""
            }
            .forEach { line ->
                // command
                if (line.startsWith("$")) {
                    if (line.startsWith("$ cd")) handleCd(line)
                } else {
                    handleLsLine(line)
                }
            }
    }

    fun handleCd(line: String) {
        val directoryName = line.replaceFirst("$ cd ", "")
        currentDirectory = if (directoryName == "..") {
            currentDirectory.parent!!
        } else {
            currentDirectory.contents.find {
                it is Directory && it.name == directoryName
            } as Directory
        }
    }

    fun handleLsLine(line: String) {
        val node = if (line.startsWith("dir")) {
            val directoryName = line.replaceFirst("dir ", "")
            Directory(directoryName, currentDirectory)

        } else {
            val splitLine = line.split(" ")
            File(splitLine[1], splitLine[0].toInt())
        }

        currentDirectory.contents.add(node)
    }

}