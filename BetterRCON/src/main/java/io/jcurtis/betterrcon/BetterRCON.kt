package io.jcurtis.betterrcon

import com.sun.net.httpserver.HttpServer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class BetterRCON : JavaPlugin() {
    private var server: HttpServer? = null

    override fun onEnable() {
        saveDefaultConfig()

        server = Servlet(this).start()
        if (server == null) {
            pluginLoader.disablePlugin(this)
        } else {
            logger.info("API is running on port ${config.getInt("port")}")
        }
    }

    override fun onDisable() {
        server?.stop(0)
    }

    fun runCmd(cmd: String) {
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
            }
        }.runTask(this)
    }
}