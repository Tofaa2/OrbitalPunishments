package net.orbitalstudios.commands

import net.orbitalstudios.OrbitalPunishments.plugin
import net.orbitalstudios.utils.colored
import net.orbitalstudios.utils.sendHelp
import org.bukkit.command.CommandSender
import java.util.*

class MainCommand : AbstractCommand(
    "orbitalpunishments",
    "The main OrbitalPunishments command",
    usage = "/orbitalpunishments"
) {

    override fun run(sender: CommandSender, cmdName: String, args: Array<out String>) {
        if (args.isEmpty()) {
            sender.sendMessage("&cOrbitalPunishments v${plugin.description.version}".colored())
            return
        }
        when (args[0].lowercase(Locale.getDefault())) {
            "reload" -> {
                plugin.reloadConfig()
                sender.sendMessage("&aReloaded config".colored())
            }
            "help" -> {
                sender.sendHelp()
            }
            else -> sender.sendMessage("Unknown subcommand")
        }
    }

}