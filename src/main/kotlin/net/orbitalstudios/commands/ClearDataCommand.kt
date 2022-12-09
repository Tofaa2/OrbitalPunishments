package net.orbitalstudios.commands

import net.orbitalstudios.data.Database
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class ClearDataCommand : AbstractCommand(
    "clearpunishmentdata",
    "Clears all punishment data",
    usage = "/clearpunishmentdata"
) {

    override fun run(sender: CommandSender, cmdName: String, args: Array<out String>) {
        if (sender !is ConsoleCommandSender) {
            sender.sendMessage("§cYou must be a console to use this command.")
            return
        }

        Database.clearAllData()
        sender.sendMessage("§aCleared all punishment data.")
    }
}