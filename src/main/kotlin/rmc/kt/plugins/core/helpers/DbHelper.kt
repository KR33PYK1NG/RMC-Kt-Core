package rmc.kt.plugins.core.helpers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Consumer
import kotlin.concurrent.thread

/**
 * Разработано командой RMC, 2021
 */
class DbHelper {

    companion object {

        private val pending = ConcurrentLinkedQueue<Triple<String, Array<out Any>, Consumer<List<Map<String, Any>>>?>>()

        /**
         * Выполняет запрос к базе данных.
         *
         * @param sql Запрос на SQL
         * @param args Аргументы вместо ?
         */
        @JvmStatic
        fun executeUpdate(sql: String,
                          vararg args: Any) {
            pending.add(Triple(sql, args, null))
            LogHelper.debug("Scheduled database request: $sql, args: ${args.joinToString()}")
        }

        /**
         * Выполняет запрос к базе данных с ответом.
         *
         * Блокирует текущий поток до завершения.
         *
         * @param action Действие над ответом
         * @param sql Запрос на SQL
         * @param args Аргументы вместо ?
         */
        @JvmStatic
        fun executeQueryBlocking(action: Consumer<List<Map<String, Any>>>,
                                 sql: String,
                                 vararg args: Any) {
            var done = false
            val lock = Object()
            var output: List<Map<String, Any>>? = null
            pending.add(Triple(sql, args, Consumer {
                output = it
                synchronized(lock) {
                    done = true
                    lock.notify()
                }
            }))
            LogHelper.debug("Scheduled database query: $sql, args: ${args.joinToString()}}")
            synchronized(lock) {
                while (!done) {
                    lock.wait()
                }
            }
            action.accept(output!!)
        }

        internal fun startDbThread(jdbcUrl: String,
                                   username: String,
                                   password: String,
                                   connectionTimeout: Long) {
            thread(name = "RMC Database Thread") {
                val hikari = HikariDataSource(HikariConfig().apply {
                    this.jdbcUrl = jdbcUrl
                    this.username = username
                    this.password = password
                    this.connectionTimeout = connectionTimeout
                })
                while (true) {
                    sleep(1)
                    if (pending.isEmpty()) {
                        continue
                    }
                    val req = pending.remove()
                    try {
                        hikari.connection.use {
                            it.prepareStatement(req.first).use {
                                var shift = 1
                                for (arg in req.second) {
                                    it.setObject(shift++, arg)
                                }
                                if (req.third != null) {
                                    val output = mutableListOf<Map<String, Any>>()
                                    val set = it.executeQuery()
                                    val meta = set.metaData
                                    while (set.next()) {
                                        val columns = mutableMapOf<String, Any>()
                                        for (i in 1..meta.columnCount) {
                                            columns.put(meta.getColumnName(i), set.getObject(i))
                                        }
                                        output.add(columns)
                                    }
                                    req.third!!.accept(output)
                                } else {
                                    it.executeUpdate()
                                }
                                LogHelper.debug("Processed database request: ${req.first}, args: ${req.second.joinToString()}")
                            }
                        }
                    } catch (stack: Throwable) {
                        LogHelper.trace(stack)
                        req.third?.accept(listOf())
                    }
                }
            }
        }

        internal fun waitForDbThread() {
            while (pending.isNotEmpty()) {}
        }

    }

}
