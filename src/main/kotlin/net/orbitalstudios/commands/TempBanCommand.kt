package net.orbitalstudios.commands

import net.orbitalstudios.OrbitalPunishments
import net.orbitalstudios.punishment.PunishmentManager
import net.orbitalstudios.utils.colored
import net.orbitalstudios.utils.sendInvalidArgs
import net.orbitalstudios.utils.sendInvalidPlayer
import net.orbitalstudios.utils.usernameToUUID
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TempBanCommand : AbstractCommand(
    "tempban",
    "Temporarily bans a user from the server",
    usage = "/tempban <username> <duration> <reason>"
) {

    override fun run(sender: CommandSender, cmdName: String, args: Array<out String>) {
        if (args.size < 3 || args[1].isEmpty() || args[2].isEmpty()) {
            sender.sendInvalidArgs()
            return
        }
        val target = args[0]
        val duration = args[1]
        val reason = args.slice(2 until args.size).joinToString(" ").colored()

        val player = target.usernameToUUID() ?: run {
            sender.sendInvalidPlayer(target)
            return
        }

        val time = parseDuration(duration)
        if (time == 0L) {
            sender.sendInvalidArgs()
            return
        }

        OrbitalPunishments.punishmentManager.tempBan(
            player,
            reason = reason.colored(),
            time = time,
            punisher = if (sender is Player) sender.uniqueId else PunishmentManager.console
        )
    }

    private fun parseDuration(duration: String): Long {
        if (duration.contains("s")) {
            val time = duration.substring(0, duration.indexOf("s"))
            if (time.isEmpty()) return 0 // Invalid duration
            return time.toLong() * 1000
        }
        if (duration.contains("m")) {
            val time = duration.substring(0, duration.indexOf("m"))
            if (time.isEmpty()) return 0 // Invalid duration
            return time.toLong() * 1000 * 60
        }
        if (duration.contains("h")) {
            val time = duration.substring(0, duration.indexOf("h"))
            if (time.isEmpty()) return 0 // Invalid duration
            return time.toLong() * 1000 * 60 * 60
        }
        if (duration.contains("d")) {
            val time = duration.substring(0, duration.indexOf("d"))
            if (time.isEmpty()) return 0 // Invalid duration
            return time.toLong() * 1000 * 60 * 60 * 24
        }
        if (duration.contains("m")) {
            val time = duration.substring(0, duration.indexOf("m"))
            if (time.isEmpty()) return 0 // Invalid duration
            return time.toLong() * 1000 * 60 * 60 * 24 * 30
        }
        if (duration.contains("y")) {
            val time = duration.substring(0, duration.indexOf("y"))
            if (time.isEmpty()) return 0 // Invalid duration
            return time.toLong() * 1000 * 60 * 60 * 24 * 30 * 12
        }
        return 0 // Invalid duration
    }


}