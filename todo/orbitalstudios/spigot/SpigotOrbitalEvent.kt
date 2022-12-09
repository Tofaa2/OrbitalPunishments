package net.orbitalstudios.spigot

import net.orbitalstudios.event.OrbitalEvent
import net.orbitalstudios.manager.Punishment
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class SpigotOrbitalEvent(val punishment: Punishment) : OrbitalEvent, Event(true) { // Async is true

    private val handlers = HandlerList()


    override fun getHandlers(): HandlerList {
        return handlers
    }
}