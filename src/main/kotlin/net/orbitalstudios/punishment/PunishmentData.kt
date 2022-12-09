package net.orbitalstudios.punishment

import net.orbitalstudios.utils.colored
import java.net.InetSocketAddress
import java.util.UUID

data class PunishmentData(
    val punishments: MutableList<Punishment>,
    val pastAddresses: MutableList<InetSocketAddress>
)
{

    fun incrementPunishment(type: PunishmentType, reason: String, issuer: UUID) {
        punishments.add(Punishment(type, reason, issuer,  System.currentTimeMillis()))
    }

    fun incrementPunishment(punishment: Punishment) {
        punishments.add(punishment)
    }

    fun getActivePunishments(): List<Punishment> {
        return punishments.filter { punishment ->
            when (punishment.type) {
                PunishmentType.KICK -> false
                else -> {
                    punishment.isCurrentlyActive()
                }
            }
        }
    }

    fun getActivePunishment(type: PunishmentType): Punishment? {
        if (type == PunishmentType.KICK) return null
        return punishments.firstOrNull() {
            it.isCurrentlyActive()
        }
    }

    fun isPassedAddress(address: InetSocketAddress): Boolean {
        return pastAddresses.contains(address)
    }

    fun isPassedAddress(address: String): Boolean {
        return pastAddresses.stream().filter {
            it.address.hostAddress == address
        }.findFirst().isPresent
    }

    fun exportAsLogLore(): List<String> {
        val lore = mutableListOf<String>()
        lore.add("&7&m----------------------".colored())
        lore.add("&7Punishments:".colored())
        lore.add("&7&m----------------------".colored())
        if (punishments.isEmpty()) {
            lore.add("&7No punishments".colored())
            return lore
        }
        punishments.forEach { punishment ->
            lore.add("&7&m----------------------".colored())
            lore.add("&7Type: &f${punishment.type.name}".colored())
            lore.add("&7Reason: &f${punishment.reason}".colored())
            lore.add("&7Issuer: &f${if (punishment.issuer == PunishmentManager.console) "Console" else punishment.issuer}".colored())
            lore.add("&7Expired: &f${if (punishment.isCurrentlyActive() && punishment.type != PunishmentType.KICK) "No" else "Yes"}".colored())
            lore.add("&7Time: &f${punishment.time}".colored())
            lore.add("&7Duration: &f${punishment.duration}".colored())
            lore.add("&7&m----------------------".colored())
        }
        return lore
    }

    fun getCurrentTempBan(): Punishment? {
        return punishments.firstOrNull {
            it.type == PunishmentType.TEMP_BAN && it.isCurrentlyActive()
        }
    }

    fun getCurrentPermBan(): Punishment? {
        return punishments.firstOrNull {
            it.type == PunishmentType.BAN && it.isCurrentlyActive()
        }
    }

    fun getCurrentBlacklist(): Punishment? {
        return punishments.firstOrNull {
            it.type == PunishmentType.BLACKLIST
        }
    }

    fun isCurrentlyBlacklisted(): Boolean {
        return getCurrentBlacklist() != null
    }

    fun isCurrentlyMuted(): Boolean {
        return (getCurrentMute() != null || getCurrentTempMute() != null)
    }

    fun getCurrentMute(): Punishment? {
        return punishments.firstOrNull {
            it.type == PunishmentType.MUTE && it.isCurrentlyActive()
        }
    }

    fun getCurrentTempMute(): Punishment? {
        return punishments.firstOrNull {
            it.type == PunishmentType.TEMP_MUTE && it.isCurrentlyActive()
        }
    }

}