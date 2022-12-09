package net.orbitalstudios.punishment

import net.orbitalstudios.OrbitalPunishments
import net.orbitalstudios.data.Database
import net.orbitalstudios.event.AsyncPlayerPunishEvent
import net.orbitalstudios.utils.colored
import net.orbitalstudios.utils.runAsync
import org.bukkit.Bukkit
import java.util.UUID

class PunishmentManager {

    private fun punish(uuid: UUID, punishment: Punishment) {
        Database.getData(uuid).thenAccept {
            it.incrementPunishment(punishment)
            Database.setData(uuid, it)
        }

        val msgs = getStringFor(uuid, punishment)
        if (OrbitalPunishments.config.getBoolean("show-messages-to-all")) {
            Bukkit.broadcastMessage(
                msgs.second.colored()
            )
            return
        }
        else if (OrbitalPunishments.config.getBoolean("show-messages-to-staff")) {
            Bukkit.getOnlinePlayers().forEach {
                if (it.hasPermission("orbitalpunishments.staff")) {
                    it.sendMessage(msgs.second.colored())
                }
            }
                OrbitalPunishments.plugin.logger.info(msgs.second)
        }
        Runnable {
            Bukkit.getServer().pluginManager.callEvent(AsyncPlayerPunishEvent(uuid, punishment))
        }.runAsync() // Need to do this cause bukkit is stupid
    }

    private fun punishKicking(uuid: UUID, punishment: Punishment) {
        punish(uuid, punishment)
        if (Bukkit.getServer().getPlayer(uuid) != null) {
            Bukkit.getServer().getPlayer(uuid)!!.kickPlayer(punishment.reason.colored())
        }
    }

    fun kick(uuid: UUID,
             punisher: UUID = console, // If no punisher is specified, it is assumed to be the console
             reason: String){
        punishKicking(uuid, Punishment(PunishmentType.KICK, reason, punisher,  System.currentTimeMillis()))
    }

    fun ban(uuid: UUID,
            punisher: UUID = console,
            reason: String){
        punishKicking(uuid, Punishment(PunishmentType.BAN, reason, punisher,  System.currentTimeMillis()))
    }

    fun tempBan(uuid: UUID,
                punisher: UUID = console,
                reason: String,
                time: Long){
        punishKicking(uuid, Punishment(PunishmentType.TEMP_BAN, reason, punisher,  System.currentTimeMillis(), time))
    }

    fun blacklist(uuid: UUID,
                    punisher: UUID = console,
                    reason: String){
            punishKicking(uuid, Punishment(PunishmentType.BLACKLIST, reason, punisher,  System.currentTimeMillis()))
    }

    fun unban(uuid: UUID) {
        Database.getData(uuid).thenAccept {
            it.punishments.forEach { punishment ->
                if (punishment.type == PunishmentType.BAN || punishment.type == PunishmentType.TEMP_BAN) {
                    punishment.expired = true
                }
            }
            Database.setData(uuid, it)
        }
    }

    fun unmute(uuid: UUID) {
        Database.getData(uuid).thenAccept {
            it.punishments.forEach { punishment ->
                if (punishment.type == PunishmentType.MUTE || punishment.type == PunishmentType.TEMP_MUTE) {
                    punishment.expired = true
                }
            }
            Database.setData(uuid, it)
        }
    }

    fun mute(uuid: UUID,
             punisher: UUID = console,
             reason: String){
        punish(uuid, Punishment(PunishmentType.MUTE, reason, punisher,  System.currentTimeMillis()))
        if (Bukkit.getServer().getPlayer(uuid) != null) {
            Bukkit.getServer().getPlayer(uuid)!!.sendMessage(reason.colored())
        }
    }

    fun tempMute(uuid: UUID,
                 punisher: UUID = console,
                 reason: String,
                 time: Long){
        punish(uuid, Punishment(PunishmentType.TEMP_MUTE, reason, punisher,  System.currentTimeMillis(), time))
        if (Bukkit.getServer().getPlayer(uuid) != null) {
            Bukkit.getServer().getPlayer(uuid)!!.sendMessage(reason.colored())
        }
    }

    companion object {
        val console: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    }

    /* Returns for staff and for user */
    fun getStringFor(uuid: UUID, punishment: Punishment): Pair<String, String> {
        var issuer = if (punishment.issuer == console) "Console" else Bukkit.getOfflinePlayer(punishment.issuer).name
        if (issuer == null) issuer = "Console"
        return when (punishment.type) {
            PunishmentType.KICK ->
                Pair(
                    OrbitalPunishments.config.getString("messages.kick.for-user")!!
                    .replace("%reason%", punishment.reason)
                    .replace("%issuer%", issuer)
                    .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!),

                    OrbitalPunishments.config.getString("messages.kick.for-staff")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!)
                )


            PunishmentType.TEMP_MUTE -> {
                Pair(
                    OrbitalPunishments.config.getString("messages.temp-mute.for-user")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!)
                        .replace("%time%", punishment.getHumanReadableExpirationDate()),

                    OrbitalPunishments.config.getString("messages.temp-mute.for-staff")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!)
                        .replace("%time%", punishment.getHumanReadableExpirationDate())
                )
            }

            PunishmentType.MUTE -> {
                Pair(
                    OrbitalPunishments.config.getString("messages.mute.for-user")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!),

                    OrbitalPunishments.config.getString("messages.mute.for-staff")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!)
                )
            }

            PunishmentType.TEMP_BAN -> {
                Pair(
                    OrbitalPunishments.config.getString("messages.temp-ban.for-user")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!)
                        .replace("%time%", punishment.getHumanReadableExpirationDate()),

                    OrbitalPunishments.config.getString("messages.temp-ban.for-staff")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!)
                        .replace("%time%", punishment.getHumanReadableExpirationDate())
                )
            }

            PunishmentType.BAN -> {
                Pair(
                    OrbitalPunishments.config.getString("messages.ban.for-user")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!),

                    OrbitalPunishments.config.getString("messages.ban.for-staff")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!)
                )
            }
            PunishmentType.BLACKLIST -> {
                Pair(
                    OrbitalPunishments.config.getString("messages.blacklist.for-user")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!),

                    OrbitalPunishments.config.getString("messages.blacklist.for-staff")!!
                        .replace("%reason%", punishment.reason)
                        .replace("%issuer%", issuer)
                        .replace("%player%", Bukkit.getOfflinePlayer(uuid).name!!)
                )
            }
        }
    }
}