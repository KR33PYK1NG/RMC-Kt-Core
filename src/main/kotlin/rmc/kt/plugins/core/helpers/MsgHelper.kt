package rmc.kt.plugins.core.helpers

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/**
 * Разработано командой RMC, 2021
 */
class MsgHelper {

    companion object {

        /**
         * Возвращает форматированную строку.
         *
         * Переводит & в цветовые коды.
         *
         * @param str Строка с & символами
         */
        @JvmStatic
        fun format(str: String): String {
            return ChatColor.translateAlternateColorCodes('&', str)
        }

        /**
         * Отправляет игроку сообщение.
         *
         * @param receiver Получатель
         * @param msg Сообщение игроку
         */
        @JvmStatic
        fun send(receiver: CommandSender, msg: String) {
            receiver.sendMessage(msg)
        }

        /**
         * Отправляет игроку сообщение.
         *
         * Переводит & в цветовые коды.
         *
         * @param receiver Получатель
         * @param msg Сообщение игроку
         */
        @JvmStatic
        fun sendFormatted(receiver: CommandSender, msg: String) {
            receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', msg))
        }

    }

}
