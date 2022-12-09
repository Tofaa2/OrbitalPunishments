package net.orbitalstudios.user

import net.orbitalstudios.manager.Punishment
import java.util.UUID

interface OrbitalUser {

    val uuid: UUID
    val lastKnownUsername: String

    val punishmentHistory: MutableList<Punishment>

}