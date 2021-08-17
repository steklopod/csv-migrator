package csv.migrator

import csv.migrator.service.Converter.migrateIserve
import mu.KotlinLogging

@ExperimentalStdlibApi
open class App {
    companion object {
        private val logger = KotlinLogging.logger {}

        @JvmStatic
        fun main(args: Array<String?>) {
            migrateIserve(args.isNotEmpty())
            logger.info { "\uD83D\uDC4C OK: все сконвертировано" }
        }
    }

}

