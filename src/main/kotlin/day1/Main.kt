package day1

import util.loadInput
import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpCookie
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.util.stream.IntStream

fun main() {
    Solution().run()
}

class Solution {

    private var input: String? = null

    fun run() {
        input = loadInput("https://adventofcode.com/2022/day/1/input")
        processInput()
    }

    fun processInput() {
        val caloriesArray = input?.replace("\n", " ")?.split("  ")
        val sumArray = caloriesArray?.map { it.split(" ").sumOf { entry -> if (entry == "") 0 else Integer.parseInt(entry) } }?.sortedDescending()!!
        val top3Sum = IntStream.range(0, 3).map { sumArray[it] }.sum()
        println(top3Sum)
    }

}