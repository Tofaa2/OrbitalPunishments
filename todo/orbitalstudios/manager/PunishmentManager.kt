package net.orbitalstudios.manager

import net.orbitalstudios.user.OrbitalUser
import java.util.UUID

interface PunishmentManager {

    fun addPunishment(punishment: Punishment)

    fun removePunishment(punishment: Punishment)

    fun getPunishment(id: UUID): Punishment?

    fun getPunishmentsOf(user: OrbitalUser): List<Punishment>

    fun getPunishmentsOf(user: OrbitalUser, type: PunishmentType): List<Punishment>

    fun getPunishmentsOf(user: OrbitalUser, type: PunishmentType, active: Boolean): List<Punishment>

    fun getPunishmentsOf(user: OrbitalUser, active: Boolean): List<Punishment>

    fun getUser(uuid: UUID): OrbitalUser?

    /* Methods for the platform specific classes */
    fun getUser(any: Any): OrbitalUser?

    fun getPlatformUser(any: Any): Any?

    fun getPlatformUser(uuid: UUID): Any?

    fun getPlatformUser(user: OrbitalUser): Any?

}