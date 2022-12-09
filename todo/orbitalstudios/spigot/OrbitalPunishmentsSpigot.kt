package net.orbitalstudios.spigot

import net.orbitalstudios.OrbitalPunishments
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * Incase in the future we want to expand the plugin to other platforms, we'll have a separate class for each platform.
 * This class is for Spigot.
 * All platforms access the same code through the [OrbitalPunishments] object.
 */
class OrbitalPunishmentsSpigot : JavaPlugin() {

    companion object {
        lateinit var instance: OrbitalPunishmentsSpigot
    }

    override fun onEnable() {
        instance = this
        OrbitalPunishments.onEnable()

        /* Setting up the platform specific variables */
        OrbitalPunishments.localDir = dataFolder
        OrbitalPunishments.config = SpigotConfigFile(File(dataFolder, "config.yml"))
        OrbitalPunishments.eventManager = SpigotEventManager()
    }

    override fun onDisable() {
        OrbitalPunishments.onDisable()
    }

}