package net.orbitalstudios.spigot

import net.orbitalstudios.event.EventManager
import net.orbitalstudios.event.OrbitalEvent
import org.bukkit.Bukkit

class SpigotEventManager : EventManager {

    override fun postEvent(event: OrbitalEvent) {
        Bukkit.getPluginManager().callEvent(event as SpigotOrbitalEvent)
    }

}