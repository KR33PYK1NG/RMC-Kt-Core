package rmc.kt.plugins.core.base

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import rmc.kt.plugins.core.CorePlugin
import rmc.kt.plugins.core.helpers.LogHelper
import java.lang.reflect.ParameterizedType

/**
 * Разработано командой RMC, 2021
 */
abstract class ListenerBase<E: Event>: Listener {

    abstract fun onEvent(event: E)

    companion object {

        private val instances = mutableMapOf<Class<out ListenerBase<out Event>>, ListenerBase<out Event>>()

        @JvmStatic
        fun register(clazz: Class<out ListenerBase<out Event>>,
                     priority: EventPriority = EventPriority.NORMAL,
                     ignoreCancelled: Boolean = false) {
            if (!instances.containsKey(clazz)) {
                val instance = clazz.getDeclaredConstructor().newInstance()
                val evclazz = (clazz.genericSuperclass as ParameterizedType).actualTypeArguments[0]
                @Suppress("UNCHECKED_CAST")
                Bukkit.getPluginManager().registerEvent(evclazz as Class<out Event>, instance, priority, {
                        listener, event ->
                    if (event::class.java == evclazz) {
                        (listener as ListenerBase<Event>).onEvent(event)
                    }
                }, CorePlugin.instance, ignoreCancelled)
                instances.put(clazz, instance)
                LogHelper.debug("Registered RMC listener: ${clazz.name}")
            } else LogHelper.debug("Tried to register already existing RMC listener: ${clazz.name}")
        }

        @JvmStatic
        fun unregister(clazz: Class<out ListenerBase<out Event>>) {
            val instance = instances.get(clazz)
            if (instance != null) {
                HandlerList.unregisterAll(instance)
                instances.remove(clazz)
                LogHelper.debug("Unregistered RMC listener: ${clazz.name}")
            } else LogHelper.debug("Tried to unregister non-existent RMC listener: ${clazz.name}")
        }

    }

}
