package net.orbitalstudios

import net.orbitalstudios.configuration.ConfigFile
import net.orbitalstudios.event.EventManager
import java.io.File
import java.lang.System.Logger

object OrbitalPunishments {

    val logger: Logger = System.getLogger("OrbitalPunishments")

    /* These methods are set up to be called by the platform specific classes */
    lateinit var localDir: File
    lateinit var config: ConfigFile
    lateinit var eventManager: EventManager
    //lateinit var punishmentManager: PunishmentManager

    fun onEnable() {
    }

    fun onDisable() {

    }

}