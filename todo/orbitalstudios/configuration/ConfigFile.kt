package net.orbitalstudios.configuration

interface ConfigFile {


    fun save()
    fun reload()

    fun getBoolean(key: String): Boolean?
    fun getDouble(key: String): Double?
    fun getInt(key: String): Int?
    fun getString(key: String): String?
    fun getStringList(key: String): List<String>

    fun getMessage(key: String): String? {
        return getString("messages.$key")
    }

    fun getMessages(key: String): List<String> {
        return getStringList("messages.$key")
    }

    fun getDatabase(key: String): String? {
        return getString("database.$key")
    }

    fun getDbHost(): String? {
        return getDatabase("host")
    }

    fun getDbPort(): Int? {
        return getDatabase("port")?.toInt()
    }

    fun getDbName(): String? {
        return getDatabase("name")
    }

    fun getDbUsername(): String? {
        return getDatabase("username")
    }

    fun getDbPassword(): String? {
        return getDatabase("password")
    }

    fun getDbTable(): String? {
        return getDatabase("table")
    }


}