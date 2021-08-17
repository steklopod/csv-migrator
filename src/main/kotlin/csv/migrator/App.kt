package csv.migrator

import csv.migrator.service.Converter.migrateIserve
import org.slf4j.LoggerFactory

@ExperimentalStdlibApi
open class App {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)

        @JvmStatic
        fun main(args: Array<String?>) {
            migrateIserve(args.isNotEmpty())
            logger.info("\uD83D\uDC4C OK: все сконвертировано")
        }
    }

}

