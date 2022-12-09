package net.orbitalstudios.commands

import net.orbitalstudios.OrbitalPunishments
import net.orbitalstudios.punishment.Punishment
import net.orbitalstudios.punishment.PunishmentManager
import net.orbitalstudios.utils.colored
import net.orbitalstudios.utils.sendInvalidArgs
import net.orbitalstudios.utils.sendInvalidPlayer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class KickCommand : AbstractCommand("kick", "Kick a player from the server") {

    override fun run(sender: CommandSender, cmdName: String, args: Array<out String>) {
        if (args.size < 2 || args[1].isEmpty()) {
            sender.sendInvalidArgs()
            return
        }
        val target = args[0]
        val reason = args.slice(1 until args.size).joinToString(" ").colored()

        val player = Bukkit.getPlayer(target)
        if (player == null) {
            sender.sendInvalidPlayer(target)
            return
        }

        OrbitalPunishments.punishmentManager.kick(
            player.uniqueId,
            reason = reason,
            punisher = if (sender is Player) sender.uniqueId else PunishmentManager.console
        )
    }

}