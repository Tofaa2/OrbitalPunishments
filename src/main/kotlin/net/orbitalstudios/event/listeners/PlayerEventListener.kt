package net.orbitalstudios.event.listeners

import net.orbitalstudios.OrbitalPunishments
import net.orbitalstudios.data.Database
import net.orbitalstudios.utils.colored
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class PlayerEventListener : Listener {

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val data = Database.getDataSync(event.player.uniqueId)
        if (data.getCurrentTempMute() != null) {
            val d = data.getCurrentTempMute()!!
            val msg = OrbitalPunishments.punishmentManager.getStringFor(event.player.uniqueId, d)
            event.player.sendMessage(msg.first.colored())
            event.isCancelled = true
        }
        else if (data.getCurrentMute() != null) {
            val d = data.getCurrentMute()!!
            val msg = OrbitalPunishments.punishmentManager.getStringFor(event.player.uniqueId, d)
            event.player.sendMessage(msg.first.colored())
            event.isCancelled = true
        }
    }


    @EventHandler
    fun onPlayerLogin(event: AsyncPlayerPreLoginEvent) {

        val ip = event.address.hostAddress
        val isIPBanned = Database.isIPBlacklistedSync(ip)
        if (isIPBanned.first) {
            val msg = OrbitalPunishments.punishmentManager.getStringFor(event.uniqueId, isIPBanned.second!!)
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, msg.first.colored())
            return // Speed up the process
        }

        val data = Database.getDataSync(event.uniqueId)
        if (data.isCurrentlyBlacklisted()) {
            val d = data.getCurrentBlacklist()!!
            if (!d.isCurrentlyActive()) return
            val msg = OrbitalPunishments.punishmentManager.getStringFor(event.uniqueId, d)
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, msg.first.colored())
            return // Speed up the process
        }
        else if (data.getCurrentPermBan() != null) {
            val d = data.getCurrentPermBan()!!
            if (!d.isCurrentlyActive()) return
            val msg = OrbitalPunishments.punishmentManager.getStringFor(event.uniqueId, d)
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, msg.first.colored())
            return // Speed up the process
        }
        else if (data.getCurrentTempBan() != null) {
            val d = data.getCurrentTempBan()!!
            if (!d.isCurrentlyActive()) return
            val msg = OrbitalPunishments.punishmentManager.getStringFor(event.uniqueId, d)
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, msg.first.colored())
            return // Speed up the process
        }
    }
}