package day16

import util.loadInput
import kotlin.math.max

fun main() {
    var input = loadInput("https://adventofcode.com/2022/day/16/input")
//    input = "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB\n" +
//            "Valve BB has flow rate=13; tunnels lead to valves CC, AA\n" +
//            "Valve CC has flow rate=2; tunnels lead to valves DD, BB\n" +
//            "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE\n" +
//            "Valve EE has flow rate=3; tunnels lead to valves FF, DD\n" +
//            "Valve FF has flow rate=0; tunnels lead to valves EE, GG\n" +
//            "Valve GG has flow rate=0; tunnels lead to valves FF, HH\n" +
//            "Valve HH has flow rate=22; tunnel leads to valve GG\n" +
//            "Valve II has flow rate=0; tunnels lead to valves AA, JJ\n" +
//            "Valve JJ has flow rate=21; tunnel leads to valve II"
    Main(input).run2()
}

class Spot(
    val flowRate: Int,
    val tunnelIndices: MutableList<Int>
)

class State(
    val openedValves: Long,
    val currentSpot: Int,
    val elephantSpot: Int,
    val flowRateSum: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (openedValves != other.openedValves) return false
        if (currentSpot != other.currentSpot) return false
        if (elephantSpot != other.elephantSpot) return false

        return true
    }

    override fun hashCode(): Int {
        var result = openedValves.hashCode()
        result = 31 * result + currentSpot
        result = 31 * result + elephantSpot
        return result
    }
}

class Main(
    val input: String
) {
    var  maxFlowRateSum = 0

    fun run2() {
        val spotList: List<Spot> = parseInput()
        val initialState = State(0, 0, 0,0)
        var states = mutableMapOf(Pair(initialState, 0))
        maxFlowRateSum = spotList.sumOf { it.flowRate }
        var maxPressureReleased = 0

        val maxMinutes = 26
        for (i in 1 until maxMinutes) {
            println("$i: ${states.size} $maxPressureReleased")

            val multiplier = maxMinutes - i
            val newStates = mutableMapOf<State, Int>()

            states.forEach {
                var state = it.key
                val pressureReleased = it.value
                val currentSpot = spotList[state.currentSpot]
                val elephantSpot = spotList[state.elephantSpot]

                val currentStates = mutableMapOf<State, Int>()

                // elephant move
                if(!isValveOpened(state.openedValves, state.elephantSpot) && elephantSpot.flowRate != 0) {
                    val newPressureReleased = pressureReleased + elephantSpot.flowRate * multiplier
                    val newState = State(
                        openValve(state.openedValves, state.elephantSpot),
                        state.currentSpot,
                        state.elephantSpot,
                        state.flowRateSum + elephantSpot.flowRate
                    )

                    currentStates[newState] = newPressureReleased
                }

                elephantSpot.tunnelIndices.forEach { newElephantIndex ->
                    val newState = State(state.openedValves, state.currentSpot, newElephantIndex, state.flowRateSum)
                    currentStates[newState] = pressureReleased
                }

                // player move
                currentStates.forEach {
                    val elephantState = it.key
                    val pressureReleased = it.value

                    if(!isValveOpened(elephantState.openedValves, elephantState.currentSpot) && currentSpot.flowRate != 0) {
                        // case: open valve
                        val newPressureReleased = pressureReleased + currentSpot.flowRate * multiplier
                        val newState = State(openValve(elephantState.openedValves, elephantState.currentSpot), elephantState.currentSpot, elephantState.elephantSpot, elephantState.flowRateSum + currentSpot.flowRate)

                        if(((!newStates.containsKey(newState) || newStates[newState]!! < newPressureReleased)
                                    )
                            && getPressureReleasedHeuristic(newState, newPressureReleased, multiplier - 1) > maxPressureReleased
                        ) {
                            newStates[newState] = newPressureReleased
                            maxPressureReleased = max(maxPressureReleased, newPressureReleased)
                        }
                    }

                    currentSpot.tunnelIndices.forEach { spotIndex ->
                        val newState = State(elephantState.openedValves, spotIndex, elephantState.elephantSpot, elephantState.flowRateSum)

                        if ((!newStates.containsKey(newState) || newStates[newState]!! < pressureReleased)
                            && getPressureReleasedHeuristic(newState, pressureReleased, multiplier - 1) > maxPressureReleased
                        ) {
                            newStates[newState] = pressureReleased
                        }
                    }
                }
            }

            states = newStates
        }

        println(maxPressureReleased)
    }

//    fun run() {
//        val spotList: List<Spot> = parseInput()
//        val initialState = State(0, 0, 0)
//        var states = mutableMapOf(Pair(initialState, 0))
//        maxFlowRateSum = spotList.sumOf { it.flowRate }
//        var maxPressureReleased = 0
//
//        val maxMinutes = 30
//        for (i in 1 until maxMinutes) {
//            println("$i: ${states.size} $maxPressureReleased")
//
//            val multiplier = maxMinutes - i
//            val newStates = mutableMapOf<State, Int>()
//
//            states.forEach {
//                val state = it.key
//                val currentSpot = spotList[state.currentSpot]
//                if(!isValveOpened(state.openedValves, state.currentSpot) && currentSpot.flowRate != 0) {
//                    // case: open valve
//                    val newPressureReleased = it.value + currentSpot.flowRate * multiplier
//                    val newState = State(openValve(state.openedValves, state.currentSpot), state.currentSpot, state.flowRateSum + currentSpot.flowRate)
//
//                    if((!newStates.containsKey(newState) || newStates[newState]!! < newPressureReleased)
//                        && getPressureReleasedHeuristic(newState, newPressureReleased, multiplier - 1) > maxPressureReleased
//                    ) {
//                        newStates[newState] = newPressureReleased
//                        maxPressureReleased = max(maxPressureReleased, newPressureReleased)
//                    }
//                }
//
//                currentSpot.tunnelIndices.forEach { spotIndex ->
//                    val newState = State(state.openedValves, spotIndex, state.flowRateSum)
//                    val pressureReleased = it.value
//
//                    if((!newStates.containsKey(newState) || newStates[newState]!! < pressureReleased)
//                        && getPressureReleasedHeuristic(newState, pressureReleased, multiplier - 1) > maxPressureReleased) {
//                        newStates[newState] = pressureReleased
//                    }
//                }
//            }
//
//            states = newStates
//        }
//    }

    fun parseInput(): List<Spot> {
        val spotMap = sortedMapOf<String, Spot>()
        val spotRegex = getSpotRegex()
        input.lines()
            .filter { it != "" }
            .forEach { line ->
                val resultGroup = spotRegex.matchEntire(line)!!.groupValues
                val name = resultGroup[1]
                val flowRate = resultGroup[2].toInt()
                spotMap[name] = Spot(flowRate, mutableListOf())
            }

        val tunnelRegex = getTunnelRegex()
        input.lines()
            .filter { it != "" }
            .forEach { line ->
                val spotResultGroup = spotRegex.matchEntire(line)!!.groupValues
                val spot = spotMap[spotResultGroup[1]]!!

                val tunnelResultGroup = tunnelRegex.matchEntire(line)!!.groupValues
                val tunnels = tunnelResultGroup[1].split(", ")
                tunnels.forEach { targetSpot ->
                    val tunnelIndex = spotMap.entries.map { it.key }.indexOf(targetSpot)
                    spot.tunnelIndices.add(tunnelIndex)
                }
            }

        return spotMap.map { it.value }
    }

    fun getPressureReleasedHeuristic(state: State, pressureReleased: Int, minutesLeft: Int): Int {
        val flowRateDelta = maxFlowRateSum - state.flowRateSum
        return pressureReleased + minutesLeft * flowRateDelta
    }

    fun getSpotRegex(): Regex {
        return Regex("Valve ([\\w]+) has flow rate=([\\d]+).*")
    }

    fun getTunnelRegex(): Regex {
        return Regex("Valve [\\w]+ has flow rate=[\\d]+; tunnels? leads? to valves? (.*)")
    }

    fun isValveOpened(openedValves: Long, index: Int): Boolean {
        return (openedValves and (1L shl index)) > 0
    }

    fun openValve(openedValves: Long, index: Int): Long {
        return openedValves or (1L shl index)
    }
}