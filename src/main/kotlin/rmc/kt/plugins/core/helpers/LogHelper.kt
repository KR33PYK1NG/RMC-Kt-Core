package rmc.kt.plugins.core.helpers

import org.apache.logging.log4j.LogManager
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Разработано командой RMC, 2021
 */
class LogHelper {

    companion object {

        private val logger = LogManager.getLogger("RMC")

        @JvmStatic
        var showDebug = false

        @JvmStatic
        var showTrace = true

        @JvmStatic
        fun info(msg: String) {
            logger.info(msg)
        }

        @JvmStatic
        fun debug(msg: String) {
            if (showDebug) {
                logger.warn(msg)
            } else {
                logger.debug(msg)
            }
        }

        @JvmStatic
        fun trace(stack: Throwable) {
            val sw = StringWriter()
            stack.printStackTrace(PrintWriter(sw))
            if (showTrace) {
                logger.warn(sw.toString())
            } else {
                logger.trace(sw.toString())
            }
        }

    }

}
