package net.orbitalstudios

import net.orbitalstudios.commands.*
import net.orbitalstudios.data.Database
import net.orbitalstudios.event.listeners.PlayerEventListener
import net.orbitalstudios.punishment.PunishmentManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.Listener
import java.lang.reflect.Field

/**
 * TODO:
 * - Optimise database structure, rn its terrible
 * - Add structure to support other providers
 */
object OrbitalPunishments {

    lateinit var config: FileConfiguration
    lateinit var plugin: OrbitalPunishmentsPlugin

    val punishmentManager = PunishmentManager()

    fun onEnable() {
        Database.load()
        registerListeners(
            PlayerEventListener()
        )
        registerCommands(
            MainCommand(),
            ClearDataCommand(),
            KickCommand(),
            UnbanCommand(),
            BanCommand(),
            TempBanCommand(),
            BlacklistCommand(),
            MuteCommand(),
            TempMuteCommand(),
            UnmuteCommand(),
        )
    }

    fun onDisable() {
    }

    private fun registerListeners(vararg listeners: Listener) {
        listeners.forEach { plugin.server.pluginManager.registerEvents(it, plugin) }
    }

    // PaperSpigot maven servers died, so we're using the old school way
    private fun registerCommands(vararg commands: AbstractCommand) {
        val bukkitCommandMap: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        bukkitCommandMap.isAccessible = true
        val commandMap: CommandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap
        commands.forEach { commandMap.register("orbitalpunishments", it) }
    }

}