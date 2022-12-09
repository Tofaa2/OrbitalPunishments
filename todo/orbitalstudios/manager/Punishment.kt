package net.orbitalstudios.manager

import net.orbitalstudios.user.OrbitalUser
import java.time.Duration
import java.util.Date
import java.util.UUID

data class Punishment(
    val id: UUID,
    val type: PunishmentType,
    val duration: Duration,
    val timePosted: Date,
    val user: OrbitalUser
    )