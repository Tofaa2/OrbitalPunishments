package net.orbitalstudios.punishment

import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

/* Base class for defining a punishment */
data class Punishment(
    val type: PunishmentType,
    val reason: String,
    val issuer: UUID,
    val time: Long,
    var duration: Long = 0L, // 0L = permanent or instant for kicks
    var expired: Boolean = false
)
{
    fun getHumanReadableDuration(): String {
        if (duration == 0L) return "Permanent"
        val date = Date(duration)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return format.format(date)
    }

    /* YYYY-MM-DD HH:MM:SS */
    fun getHumanReadablePunishmentDate(): String {
        return Punishment.dateFormat.format(Date(time))
    }

    fun getHumanReadableExpirationDate(): String {
        return Punishment.dateFormat.format(Date(time + duration))
    }

    fun isCurrentlyActive(): Boolean {
        if (expired) return false
        if (duration == 0L) return true
        return System.currentTimeMillis() < time + duration
    }


    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }
}