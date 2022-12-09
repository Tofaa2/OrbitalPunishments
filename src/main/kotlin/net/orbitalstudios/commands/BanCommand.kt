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

class BanCommand : AbstractCommand("ban", "Bans a user from the server", usage = "/ban <username> <reason>") {

    override fun run(sender: CommandSender, cmdName: String, args: Array<out String>) {
        if (args.size < 2 || args[1].isEmpty()) {
            sender.sendInvalidArgs()
            return
        }
        val target = args[0]
        val reason = args.slice(1 until args.size).joinToString(" ").colored()

        val player = target.usernameToUUID() ?: run {
            sender.sendInvalidPlayer(target)
            return
        }

        OrbitalPunishments.punishmentManager.ban(
            player,
            reason = reason,
            punisher = if (sender is Player) sender.uniqueId else PunishmentManager.console
        )
    }

}

