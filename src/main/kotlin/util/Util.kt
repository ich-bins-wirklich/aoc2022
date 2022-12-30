package util

import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpCookie
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun loadInput(url: String) : String {
    CookieHandler.setDefault(CookieManager())
    val sessionCookie = HttpCookie(
        "session",
        "enter session cookie here, dummy"
    )
    sessionCookie.path = "/"
    sessionCookie.version = 0
    val cookieManager: CookieManager = CookieHandler.getDefault() as CookieManager
    cookieManager.cookieStore.add(URI("https://adventofcode.com/"), sessionCookie)

    val client = HttpClient
        .newBuilder()
        .cookieHandler(cookieManager)
        .build()
    val request = HttpRequest
        .newBuilder(URI.create(url))
        .GET()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}