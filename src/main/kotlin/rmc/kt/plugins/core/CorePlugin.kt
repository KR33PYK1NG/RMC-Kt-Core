package rmc.kt.plugins.core

import org.bukkit.plugin.java.JavaPlugin
import rmc.kt.plugins.core.helpers.DbHelper

/**
 * Разработано командой RMC, 2021
 */
class CorePlugin: JavaPlugin() {

    companion object {

        @JvmStatic
        lateinit var instance: CorePlugin
            private set

    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        val jdbcUrl = config.getString("hikari.jdbc_url")!!
        val username = config.getString("hikari.username")!!
        val password = config.getString("hikari.password")!!
        val connectionTimeout = config.getLong("hikari.connection_timeout")
        DbHelper.startDbThread(jdbcUrl, username, password, connectionTimeout)
    }

    override fun onDisable() {
        DbHelper.waitForDbThread()
    }

}
