package day19

import util.loadInput
import java.lang.Integer.max

fun main() {
    var input = loadInput("https://adventofcode.com/2022/day/19/input")
//    input =
//        "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.\n" +
//        "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian."
    Main(input).run()
}

class State(
    val oreCollected: Int,
    val clayCollected: Int,
    val obsidianCollected: Int,
    val geodeCollected: Int,
    val robots: IntArray
) {
    fun collectResources(): State {
        return State(
            oreCollected + robots[0],
            clayCollected + robots[1],
            obsidianCollected + robots[2],
            geodeCollected + robots[3],
            robots
        )
    }

    fun getMinProjectedGeodes(turnsLeft: Int): Int {
        return geodeCollected + robots[3] * turnsLeft
    }

    fun getMaxProjectedGeodes(turnsLeft: Int): Int {
        return getMinProjectedGeodes(turnsLeft) + (turnsLeft * (turnsLeft - 1) / 2)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (oreCollected != other.oreCollected) return false
        if (clayCollected != other.clayCollected) return false
        if (obsidianCollected != other.obsidianCollected) return false
        if (geodeCollected != other.geodeCollected) return false
        if (!robots.contentEquals(other.robots)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = oreCollected
        result = 31 * result + clayCollected
        result = 31 * result + obsidianCollected
        result = 31 * result + geodeCollected
        result = 31 * result + robots.contentHashCode()
        return result
    }

}

class Blueprint(
    val oreCosts: IntArray,
    val clayCost: Int,
    val obsidianCost: Int
) {
    fun buyRobot(state: State, robot: Int): State? {
        if(oreCosts[robot] > state.oreCollected) return null

        var oreCollected = state.oreCollected - oreCosts[robot]
        var clayCollected = state.clayCollected
        var obsidianCollected = state.obsidianCollected
        var geodeCollected = state.geodeCollected

        if(robot == 2) {
            if(state.clayCollected < clayCost) return null
            clayCollected -= clayCost
        } else if(robot == 3) {
            if(state.obsidianCollected < obsidianCost) return null
            obsidianCollected -= obsidianCost
        }

        when(robot) {
            0 -> oreCollected--
            1 -> clayCollected--
            2 -> obsidianCollected--
            3 -> geodeCollected--
        }

        val robots = state.robots.clone()
        robots[robot]++

        return State(
            oreCollected,
            clayCollected,
            obsidianCollected,
            geodeCollected,
            robots
        )
    }

    fun isWaitingSensible(state: State): Boolean {
        return state.oreCollected < oreCosts[0]
                || state.oreCollected < oreCosts[1]
                || state.robots[1] > 0 && (state.oreCollected < oreCosts[2] || state.clayCollected < clayCost)
                || state.robots[2] > 0 && (state.oreCollected < oreCosts[3] || state.obsidianCollected < obsidianCost)
    }
}

class Main(
    val input: String
) {
    companion object {
        const val ROUNDS = 32
    }

    fun run() {
        val blueprints = parseInput().filterIndexed { index, _ -> index <= 2 }
        val qualityLevelSum = blueprints.mapIndexed { index, blueprint ->
            val geodeValue = getMaxGeodeValue(blueprint)
            println("blueprint $index $geodeValue")
            geodeValue
        }.reduce { acc, value -> acc * value }
        println("result: $qualityLevelSum")
    }

    fun getMaxGeodeValue(blueprint: Blueprint): Int {
        val initialState = State(0,0, 0, 0, intArrayOf(1, 0, 0, 0))
        var states = setOf(initialState)
        var minProjectedMaxGeodeValue = 0

        for(round in 0 until ROUNDS) {
            println("$round ${states.size}")
            val newStates = mutableSetOf<State>()

            states.forEach { state ->
                val stateBatch = mutableListOf<State>()

                for(robot in 0 until 4) {
                    var newState = blueprint.buyRobot(state, robot)
                    if(newState != null) stateBatch.add(newState)
                }

                if(blueprint.isWaitingSensible(state)) stateBatch.add(state)

                val roundsLeft = ROUNDS - round - 1
                stateBatch
                    .map { it.collectResources() }
                    .filter { it.getMaxProjectedGeodes(roundsLeft) >= minProjectedMaxGeodeValue }
                    .forEach {
                        val minGeodeValue = it.getMinProjectedGeodes(roundsLeft)
                        minProjectedMaxGeodeValue = max(minProjectedMaxGeodeValue, minGeodeValue)

                        newStates.add(it)
                    }
            }

            states = newStates.toSet()
        }

        return states.maxOf { it.geodeCollected }
    }

    fun getBlueprintRegex(): Regex {
        return Regex("Blueprint [\\d]+: Each ore robot costs ([\\d]+) ore. Each clay robot costs ([\\d]+) ore. Each obsidian robot costs ([\\d]+) ore and ([\\d]+) clay. Each geode robot costs ([\\d]+) ore and ([\\d]+) obsidian.")
    }

    fun parseInput(): List<Blueprint> {
        val regex = getBlueprintRegex()
        return input.lines()
            .filter { it != "" }
            .map { line ->
                val groupValues = regex.matchEntire(line)!!.groupValues
                Blueprint(
                    intArrayOf(groupValues[1].toInt(), groupValues[2].toInt(), groupValues[3].toInt(), groupValues[5].toInt()),
                    groupValues[4].toInt(),
                    groupValues[6].toInt()
                )
            }
    }
}