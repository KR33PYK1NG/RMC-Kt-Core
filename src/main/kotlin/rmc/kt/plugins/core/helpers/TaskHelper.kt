package rmc.kt.plugins.core.helpers

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import rmc.kt.plugins.core.CorePlugin

/**
 * Разработано командой RMC, 2021
 */
class TaskHelper {

    companion object {

        /**
         * Прямо сейчас выполняет действие в основном потоке.
         *
         * @param action Целевое действие
         */
        @JvmStatic
        fun syncNow(action: Runnable): BukkitTask {
            return createTask(action).runTask(CorePlugin.instance)
        }

        /**
         * Прямо сейчас выполняет действие асинхронно к основному потоку.
         *
         * @param action Целевое действие
         */
        @JvmStatic
        fun asyncNow(action: Runnable): BukkitTask {
            return createTask(action).runTaskAsynchronously(CorePlugin.instance)
        }

        /**
         * После задержки выполняет действие в основном потоке.
         *
         * @param delay Задержка в тиках
         * @param action Целевое действие
         */
        @JvmStatic
        fun syncLater(delay: Long,
                      action: Runnable): BukkitTask {
            return createTask(action).runTaskLater(CorePlugin.instance, delay)
        }

        /**
         * После задержки выполняет действие асинхронно к основному потоку.
         *
         * @param delay Задержка в тиках
         * @param action Целевое действие
         */
        @JvmStatic
        fun asyncLater(delay: Long,
                       action: Runnable): BukkitTask {
            return createTask(action).runTaskLaterAsynchronously(CorePlugin.instance, delay)
        }

        /**
         * Через каждый промежуток выполняет действие в основном потоке.
         *
         * @param delay Задержка в тиках
         * @param period Промежуток в тиках
         * @param action Целевое действие
         */
        @JvmStatic
        fun syncTimer(delay: Long,
                      period: Long = delay,
                      action: Runnable): BukkitTask {
            return createTask(action).runTaskTimer(CorePlugin.instance, delay, period)
        }

        /**
         * Через каждый промежуток выполняет действие асинхронно к основному потоку.
         *
         * @param delay Задержка в тиках
         * @param period Промежуток в тиках
         * @param action Целевое действие
         */
        @JvmStatic
        fun asyncTimer(delay: Long,
                       period: Long = delay,
                       action: Runnable): BukkitTask {
            return createTask(action).runTaskTimerAsynchronously(CorePlugin.instance, delay, period)
        }

        private fun createTask(action: Runnable): BukkitRunnable {
            return object: BukkitRunnable() {
                override fun run() {
                    action.run()
                }
            }
        }

    }

}
