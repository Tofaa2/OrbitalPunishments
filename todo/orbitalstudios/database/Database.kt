package net.orbitalstudios.database

import net.orbitalstudios.OrbitalPunishments
import java.sql.Connection
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object Database {

    private val executor: ExecutorService = Executors.newCachedThreadPool()
    private lateinit var connection: Connection

    private val host: String = OrbitalPunishments.config.getDbHost()!!
    private val port: Int = OrbitalPunishments.config.getDbPort()!!

    fun connectAndCheck() {
        executor.execute {

        }
    }


}