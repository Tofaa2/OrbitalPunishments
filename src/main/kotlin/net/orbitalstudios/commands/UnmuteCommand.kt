package net.orbitalstudios.commands

import net.orbitalstudios.OrbitalPunishments
import net.orbitalstudios.utils.sendInvalidArgs
import net.orbitalstudios.utils.sendUnbanPlayer
import net.orbitalstudios.utils.sendUnmutePlayer
import net.orbitalstudios.utils.usernameToUUID
import org.bukkit.command.CommandSender

class UnmuteCommand : AbstractCommand(
    "unmute",
    "Unmute a player",
    usage = "/unmute <player>"
) {

    override fun run(sender: CommandSender, cmdName: String, args: Array<out String>) {
        if (args.size < 1 || args[0].isEmpty()) {
            sender.sendInvalidArgs()
            return
        }
        val target = args[0]

        if (target.isEmpty()) {
            sender.sendInvalidArgs()
            return
        }
        val a = target.usernameToUUID()
        if (a == null) {
            sender.sendInvalidArgs()
            return
        }
        OrbitalPunishments.punishmentManager.unmute(a)
        sender.sendUnmutePlayer(target)
    }

}