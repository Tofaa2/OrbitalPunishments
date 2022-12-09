package net.orbitalstudios.commands

import net.orbitalstudios.OrbitalPunishments
import net.orbitalstudios.punishment.PunishmentManager
import net.orbitalstudios.utils.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TempMuteCommand : AbstractCommand(
    "tempmute",
    "Temporarily mute a player",
    usage = "/tempmute <player> <time> <reason>",
){

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
        val time = duration.parseDuration()
        if (time == 0L) {
            sender.sendInvalidArgs()
            return
        }

        OrbitalPunishments.punishmentManager.tempMute(
            player,
            reason = reason.colored(),
            time = time,
            punisher = if (sender is Player) sender.uniqueId else PunishmentManager.console
        )
    }

}