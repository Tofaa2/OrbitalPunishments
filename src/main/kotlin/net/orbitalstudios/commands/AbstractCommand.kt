package net.orbitalstudios.commands

import net.orbitalstudios.utils.*
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import java.lang.RuntimeException

abstract class AbstractCommand(
    name: String,
    description: String,
    aliases: MutableList<String> = mutableListOf(),
    usage: String = "/$name <username>"
) : BukkitCommand(name, description, usage, aliases){

    abstract fun run(sender: CommandSender, cmdName: String, args: Array<out String>)

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (args.isEmpty() && commandLabel != "orbitalpunishments") {
            sender.sendInvalidArgs()
            return false
        }
        if (!sender.hasPermission("orbitalpunishments.$name")) {
            sender.sendNoPermission()
            return false
        }
        try {
            run(sender, commandLabel, args)
        }
        catch (e: Exception) {
            sender.sendError()
            throw PunishmentException(commandLabel, e.message.toString())
        }
        return true
    }
}

open class PunishmentException(cmd: String, message: String) : RuntimeException("Error while executing command $cmd: $message")