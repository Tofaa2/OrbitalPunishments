package net.orbitalstudios.utils

import org.bukkit.ChatColor

fun String?.colored(): String {
    return ChatColor.translateAlternateColorCodes('&', this ?: "&cNULL")
}

/* Not ideal way I'm aware, but it works for now */
fun String?.parseDuration(): Long {
    if (this==null) return 0
    if (this.contains("s")) {
        val time = this.substring(0, this.indexOf("s"))
        if (time.isEmpty()) return 0 // Invalid duration
        return time.toLong() * 1000
    }
    if (this.contains("m")) {
        val time = this.substring(0, this.indexOf("m"))
        if (time.isEmpty()) return 0 // Invalid duration
        return time.toLong() * 1000 * 60
    }
    if (this.contains("h")) {
        val time = this.substring(0, this.indexOf("h"))
        if (time.isEmpty()) return 0 // Invalid duration
        return time.toLong() * 1000 * 60 * 60
    }
    if (this.contains("d")) {
        val time = this.substring(0, this.indexOf("d"))
        if (time.isEmpty()) return 0 // Invalid duration
        return time.toLong() * 1000 * 60 * 60 * 24
    }
    if (this.contains("m")) {
        val time = this.substring(0, this.indexOf("m"))
        if (time.isEmpty()) return 0 // Invalid duration
        return time.toLong() * 1000 * 60 * 60 * 24 * 30
    }
    if (this.contains("y")) {
        val time = this.substring(0, this.indexOf("y"))
        if (time.isEmpty()) return 0 // Invalid duration
        return time.toLong() * 1000 * 60 * 60 * 24 * 30 * 12
    }
    return 0 // Invalid duration
}