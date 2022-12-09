package net.orbitalstudios.utils

import com.google.gson.JsonObject
import java.net.URI
import java.net.URL

fun URL.fetchData(): JsonObject {
    val connection = this.openConnection()
    connection.setRequestProperty("User-Agent", "Mozilla/5.0")
    val inputStream = connection.getInputStream()
    val json = inputStream.bufferedReader().use { it.readText() }
    return json.parseJson()
}

fun URI.fetchData(): JsonObject {
    val connection = this.toURL().openConnection()
    connection.setRequestProperty("User-Agent", "Mozilla/5.0")
    val inputStream = connection.getInputStream()
    val json = inputStream.bufferedReader().use { it.readText() }
    return json.parseJson()
}

fun String.parseJson(): JsonObject {
    return com.google.gson.JsonParser.parseString(this).asJsonObject
}
