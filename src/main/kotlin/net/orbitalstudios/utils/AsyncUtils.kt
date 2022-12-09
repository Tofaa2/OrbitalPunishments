package net.orbitalstudios.utils

import net.orbitalstudios.OrbitalPunishments
import org.bukkit.Bukkit

fun Runnable.runAsync() {
    Bukkit.getScheduler().runTaskAsynchronously(OrbitalPunishments.plugin, this)
}
