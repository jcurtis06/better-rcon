package io.jcurtis.betterrcon

import com.sun.net.httpserver.HttpServer
import org.bukkit.Bukkit
import java.net.BindException
import java.net.InetSocketAddress

class Servlet(private val main: BetterRCON, private val port: Int = main.config.getInt("port")) {
    fun start(): HttpServer? {
        var server: HttpServer

        try {
            server = HttpServer.create(InetSocketAddress(port), 0)
        } catch (e: BindException) {
            Bukkit.getLogger().warning("Port $port is already in use!")
            return null
        }


        server.createContext("/cmd") { exchange ->
            if (exchange.requestMethod.equals("POST")) {
                val body = exchange.requestBody.bufferedReader().readText()
                main.runCmd(body)
                exchange.sendResponseHeaders(200, 0)
            } else {
                exchange.sendResponseHeaders(405, 0)
            }
            exchange.close()
        }
        server.executor = null
        server.start()
        return server
    }
}