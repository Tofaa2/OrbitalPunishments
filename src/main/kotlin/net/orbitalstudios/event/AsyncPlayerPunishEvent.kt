package net.orbitalstudios.event

import net.orbitalstudios.punishment.Punishment
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*


class AsyncPlayerPunishEvent(punished: UUID, punishment: Punishment) : Event(true) {

    private val handlersList = HandlerList()

    override fun getHandlers(): HandlerList {
        return handlersList
    }
}