package net.orbitalstudios.spigot

import net.orbitalstudios.manager.Punishment
import net.orbitalstudios.user.OrbitalUser
import net.orbitalstudios.utils.fetchData
import java.net.URI
import java.util.*

class SpigotOrbitalUser(override val uuid: UUID) : OrbitalUser {
    override val lastKnownUsername: String
        get() = URI("https://api.mojang.com/user/profiles/$uuid/names").fetchData()["name"].asString


    override val punishmentHistory: MutableList<Punishment>
        get() = TODO("Not yet implemented")


}