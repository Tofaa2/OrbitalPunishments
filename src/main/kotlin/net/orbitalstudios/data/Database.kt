package net.orbitalstudios.data

import com.google.gson.Gson
import net.orbitalstudios.OrbitalPunishments
import net.orbitalstudios.punishment.Punishment
import net.orbitalstudios.punishment.PunishmentData
import net.orbitalstudios.punishment.PunishmentType
import net.orbitalstudios.utils.runAsync
import net.orbitalstudios.utils.usernameToUUID
import java.sql.Connection
import java.sql.DriverManager
import java.util.UUID
import java.util.concurrent.CompletableFuture

object Database {

    private val gson: Gson = Gson()

    private val connectionString: String = OrbitalPunishments.config.getString("database.jdbc-url")!!
    private val table: String = OrbitalPunishments.config.getString("database.table")!!
    private lateinit var connection: Connection

    fun load() {
        Class.forName("com.mysql.jdbc.Driver")
        connection = DriverManager.getConnection(connectionString)
        connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS $table (uuid VARCHAR(36), data TEXT)"
        ).execute()
    }

    fun getData(uuid: UUID): CompletableFuture<PunishmentData> {
        if (connection.isClosed) load()
        val future = CompletableFuture<PunishmentData>()
        Runnable {
            val statement = connection.prepareStatement("SELECT * FROM $table WHERE uuid=?")
            statement.setString(1, uuid.toString())
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val data = resultSet.getString("data")
                future.complete(gson.fromJson(data, PunishmentData::class.java) as PunishmentData)
            }
            else {
                future.complete(PunishmentData(mutableListOf(), mutableListOf()))
            }
        }.runAsync()
        return future
    }

    fun setData(uuid: UUID, data: PunishmentData) {
        if (connection.isClosed) load()
        Runnable {
            val statement = connection.prepareStatement("SELECT * FROM $table WHERE uuid=?")
            statement.setString(1, uuid.toString())
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val updateStatement = connection.prepareStatement("UPDATE $table SET data=? WHERE uuid=?")
                updateStatement.setString(1, gson.toJson(data))
                updateStatement.setString(2, uuid.toString())
                updateStatement.execute()
            } else {
                val insertStatement = connection.prepareStatement("INSERT INTO $table (uuid, data) VALUES (?, ?)")
                insertStatement.setString(1, uuid.toString())
                insertStatement.setString(2, gson.toJson(data))
                insertStatement.execute()
            }
        }.runAsync()
    }

    fun getDataSync(uuid: UUID): PunishmentData {
        if (connection.isClosed) load()
        val statement = connection.prepareStatement("SELECT * FROM $table WHERE uuid=?")
        statement.setString(1, uuid.toString())
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val data = resultSet.getString("data")
            return gson.fromJson(data, PunishmentData::class.java) as PunishmentData
        }
        else {
            return PunishmentData(mutableListOf(), mutableListOf())
        }
    }

    fun setDataSync(uuid: UUID, data: PunishmentData) {
        if (connection.isClosed) load()
        val statement = connection.prepareStatement("SELECT * FROM $table WHERE uuid=?")
        statement.setString(1, uuid.toString())
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val updateStatement = connection.prepareStatement("UPDATE $table SET data=? WHERE uuid=?")
            updateStatement.setString(1, gson.toJson(data))
            updateStatement.setString(2, uuid.toString())
            updateStatement.execute()
        } else {
            val insertStatement = connection.prepareStatement("INSERT INTO $table (uuid, data) VALUES (?, ?)")
            insertStatement.setString(1, uuid.toString())
            insertStatement.setString(2, gson.toJson(data))
            insertStatement.execute()
        }
    }

    fun getAllData(): CompletableFuture<List<PunishmentData>> {
        if (connection.isClosed) load()
        val future = CompletableFuture<List<PunishmentData>>()
        Runnable {
            val statement = connection.prepareStatement("SELECT * FROM $table")
            val resultSet = statement.executeQuery()
            val data = mutableListOf<PunishmentData>()
            while (resultSet.next()) {
                val dataString = resultSet.getString("data")
                data.add(gson.fromJson(dataString, PunishmentData::class.java) as PunishmentData)
            }
            future.complete(data)
        }.runAsync()
        return future
    }

    fun getAllDataSync(): List<PunishmentData> {
        if (connection.isClosed) load()
        val statement = connection.prepareStatement("SELECT * FROM $table")
        val resultSet = statement.executeQuery()
        val data = mutableListOf<PunishmentData>()
        while (resultSet.next()) {
            val dataString = resultSet.getString("data")
            data.add(gson.fromJson(dataString, PunishmentData::class.java) as PunishmentData)
        }
        return data
    }

    fun isIPBlacklisted(ip: String): CompletableFuture<Pair<Boolean, Punishment?>> {
        if (connection.isClosed) load()
        val future = CompletableFuture<Pair<Boolean, Punishment?>>()
        Runnable {
            val statement = connection.prepareStatement("SELECT * FROM $table")
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val dataString = resultSet.getString("data")
                val data = gson.fromJson(dataString, PunishmentData::class.java) as PunishmentData
                val ci = data.pastAddresses.firstOrNull {
                    it.address.hostAddress == ip
                }
                if (ci != null) {
                    val punishment = data.punishments.first {
                        it.type == PunishmentType.BLACKLIST
                    }
                    future.complete(Pair(true, punishment))
                }
            }
            future.complete(Pair(false, null))
        }.runAsync()
        return future
    }

    fun isIPBlacklistedSync(ip: String): Pair<Boolean, Punishment?> {
        if (connection.isClosed) load()
        val statement = connection.prepareStatement("SELECT * FROM $table")
        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            val dataString = resultSet.getString("data")
            val data = gson.fromJson(dataString, PunishmentData::class.java) as PunishmentData
            val ci = data.pastAddresses.firstOrNull {
                it.address.hostAddress == ip
            }
            if (ci != null) {
                val punishment = data.punishments.first {
                    it.type == PunishmentType.BLACKLIST
                }
                return Pair(true, punishment)
            }
        }
        return Pair(false, null)
    }

    fun clearData(uuid: UUID) {
        val statement = connection.prepareStatement("DELETE FROM $table WHERE uuid=?")
        statement.setString(1, uuid.toString())
        statement.execute()
    }

    fun clearAllData() {
        val statement = connection.prepareStatement("DELETE FROM $table")
        statement.execute()
    }

}