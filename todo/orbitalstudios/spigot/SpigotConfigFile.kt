package net.orbitalstudios.spigot

import net.orbitalstudios.configuration.ConfigFile
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class SpigotConfigFile(private val file: File) : ConfigFile {

    private var config: YamlConfiguration

    init {
        if (!file.exists()) {
            OrbitalPunishmentsSpigot.instance.saveResource(file.name, true)
        }
        config = YamlConfiguration.loadConfiguration(file)
    }



    override fun getBoolean(key: String): Boolean {
        return config.getBoolean(key)
    }

    override fun getDouble(key: String): Double {
        return config.getDouble(key)
    }

    override fun getInt(key: String): Int {
        return config.getInt(key)
    }

    override fun getString(key: String): String? {
        return config.getString(key)
    }

    override fun getStringList(key: String): List<String> {
        return config.getStringList(key)
    }

    override fun save() {
        OrbitalPunishmentsSpigot.instance.saveResource(file.name, true)
    }

    override fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
    }
}