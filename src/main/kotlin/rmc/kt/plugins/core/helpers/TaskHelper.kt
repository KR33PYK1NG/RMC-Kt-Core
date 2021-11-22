package rmc.kt.plugins.core.helpers

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import rmc.kt.plugins.core.CorePlugin

/**
 * Разработано командой RMC, 2021
 */
class TaskHelper {

    companion object {

        @JvmStatic
        fun syncNow(action: Runnable): BukkitTask {
            return createTask(action).runTask(CorePlugin.instance)
        }

        @JvmStatic
        fun asyncNow(action: Runnable): BukkitTask {
            return createTask(action).runTaskAsynchronously(CorePlugin.instance)
        }

        @JvmStatic
        fun syncLater(action: Runnable,
                      delay: Long): BukkitTask {
            return createTask(action).runTaskLater(CorePlugin.instance, delay)
        }

        @JvmStatic
        fun asyncLater(action: Runnable,
                       delay: Long): BukkitTask {
            return createTask(action).runTaskLaterAsynchronously(CorePlugin.instance, delay)
        }

        @JvmStatic
        fun syncTimer(action: Runnable,
                      delay: Long,
                      period: Long = delay): BukkitTask {
            return createTask(action).runTaskTimer(CorePlugin.instance, delay, period)
        }

        @JvmStatic
        fun asyncTimer(action: Runnable,
                       delay: Long,
                       period: Long = delay): BukkitTask {
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
