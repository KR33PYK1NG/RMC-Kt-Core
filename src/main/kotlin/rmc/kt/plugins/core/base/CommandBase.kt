package rmc.kt.plugins.core.base

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import rmc.kt.plugins.core.helpers.LogHelper

/**
 * Разработано командой RMC, 2021
 */
abstract class CommandBase: Command("") {

    abstract fun onCommand(sender: CommandSender, label: String, args: Array<String>)

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        onCommand(sender, commandLabel, args)
        return true
    }

    companion object {

        private val instances = mutableMapOf<Class<out CommandBase>, CommandBase>()

        @JvmStatic
        fun register(clazz: Class<out CommandBase>,
                     label: String,
                     vararg aliases: String) {
            if (!instances.containsKey(clazz)) {
                val instance = clazz.getDeclaredConstructor().newInstance()
                instance.label = label
                instance.aliases = aliases.asList()
                val known = getKnownCommands()
                known.put(instance.label, instance)
                known.put("rmc:${instance.label}", instance)
                for (alias in instance.aliases) {
                    known.put(alias, instance)
                    known.put("rmc:$alias", instance)
                }
                instances.put(clazz, instance)
                LogHelper.debug("Registered RMC command: ${clazz.name}")
            } else LogHelper.debug("Tried to register already existing RMC command: ${clazz.name}")
        }

        @JvmStatic
        fun unregister(clazz: Class<out CommandBase>) {
            val instance = instances.get(clazz)
            if (instance != null) {
                val known = getKnownCommands()
                known.remove(instance.label)
                known.remove("rmc:${instance.label}")
                for (alias in instance.aliases) {
                    known.remove(alias)
                    known.remove("rmc:$alias")
                }
                instances.remove(clazz)
                LogHelper.debug("Unregistered RMC command: ${clazz.name}")
            } else LogHelper.debug("Tried to unregister non-existent RMC command: ${clazz.name}")
        }

        private fun getKnownCommands(): MutableMap<String, Command> {
            return try {
                val cmapField = Bukkit.getPluginManager()::class.java.getDeclaredField("commandMap")
                cmapField.trySetAccessible()
                val cmapObj = cmapField.get(Bukkit.getPluginManager())
                @Suppress("UNCHECKED_CAST")
                try {
                    val knownMethod = cmapObj::class.java.getMethod("getKnownCommands")
                    knownMethod.invoke(cmapObj)
                } catch (ignore: Throwable) {
                    val knownField = cmapObj::class.java.getDeclaredField("knownCommands")
                    knownField.trySetAccessible()
                    knownField.get(cmapObj)
                } as MutableMap<String, Command>
            } catch (stack: Throwable) {
                LogHelper.trace(stack)
                mutableMapOf()
            }
        }

    }

}
