package net.orbitalstudios

import org.bukkit.plugin.java.JavaPlugin

class OrbitalPunishmentsPlugin : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        OrbitalPunishments.config = config
        OrbitalPunishments.plugin = this
        OrbitalPunishments.onEnable()
    }

    override fun onDisable() {
        OrbitalPunishments.onDisable()
    }
}