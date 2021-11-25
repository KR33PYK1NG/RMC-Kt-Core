package rmc.kt.plugins.core.helpers

import org.bukkit.ChatColor

/**
 * Разработано командой RMC, 2021
 */
class ChatHelper {

    companion object {

        /**
         * Возвращает форматированную строку.
         *
         * Переводит символ & в цветовые коды.
         *
         * @param str Строка с символами &
         */
        @JvmStatic
        fun format(str: String): String {
            return ChatColor.translateAlternateColorCodes('&', str)
        }

    }

}
