package net.orbitalstudios.utils

import com.google.gson.JsonObject
import net.orbitalstudios.OrbitalPunishments
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.net.URI
import java.util.UUID

fun CommandSender.sendMsg(path: String) {
    val msg = OrbitalPunishments.config.getString("messages.$path").colored()
    this.sendMessage(msg)
}

fun CommandSender.sendNoPermission() {
    this.sendMsg("no-permission")
}

fun CommandSender.sendInvalidArgs() {
    this.sendMsg("invalid-args")
}

fun CommandSender.sendHelp() {
    val list = OrbitalPunishments.config.getStringList("messages.help")
    list.forEach { this.sendMessage(ChatColor.translateAlternateColorCodes('&', it)) }
}

fun CommandSender.sendError() {
    this.sendMsg("error")
}

fun CommandSender.sendInvalidPlayer(target: String) {
    val component = OrbitalPunishments.config.getString("messages.invalid-player").colored().replace("%player%", target)
    this.sendMessage(component)
}

fun CommandSender.sendUnbanPlayer(target: String) {
    val component = OrbitalPunishments.config.getString("messages.unban-player").colored().replace("%player%", target)
    this.sendMessage(component)
}

fun CommandSender.sendUnmutePlayer(target: String) {
    val component = OrbitalPunishments.config.getString("messages.unmute-player").colored().replace("%player%", target)
    this.sendMessage(component)
}


fun CommandSender.sendTempMutePlayer(target: String, reason: String, duration: String) {
    val component = OrbitalPunishments.config.getString("messages.mute.staff-msg-broadcast").colored()
        .replace("%player%", target)
        .replace("%reason%", reason)
        .replace("%duration%", duration)
    this.sendMessage(component)
}












fun UUID.getMojangData(): JsonObject {
    val url = URI("https://sessionserver.mojang.com/session/minecraft/profile/${this}")
    return url.fetchData()
}

/* Username */
fun String.getMojangData(): JsonObject {
    val url = URI("https://api.mojang.com/users/profiles/minecraft/$this")
    val uuidString = url.fetchData().get("id").asString
    val uuid = UUID.fromString("${uuidString.substring(0, 8)}-${uuidString.substring(8, 12)}-${uuidString.substring(12, 16)}-${uuidString.substring(16, 20)}-${uuidString.substring(20, 32)}")
    return uuid.getMojangData()
}

fun String.usernameToUUID(): UUID? {
    val url = URI("https://api.mojang.com/users/profiles/minecraft/$this")
    val uuidString = url.fetchData().get("id").asString
    return UUID.fromString("${uuidString.substring(0, 8)}-${uuidString.substring(8, 12)}-${uuidString.substring(12, 16)}-${uuidString.substring(16, 20)}-${uuidString.substring(20, 32)}")
}