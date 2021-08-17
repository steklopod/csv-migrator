package csv.migrator.service

import csv.migrator.model.Iserve
import csv.migrator.model.Migration
import mu.KotlinLogging

@ExperimentalStdlibApi
object Converter {
    private val logger = KotlinLogging.logger {}

    fun migrateIserve(withExt: Boolean): List<List<String?>> {
        val migrationsMap = Reader.readMigrationsMap()
        val iserveUpdated: List<Iserve> = updateIserveLogins(migrationsMap, withExt)
        return Writer.writeIserve(iserveUpdated)
    }

    private fun updateIserveLogins(migrationsMap: Map<String?, Migration>, withExt: Boolean): List<Iserve> =
        Reader.readIserve().map {
            migrationsMap[it.managerTubNumber]?.NEW_LOGIN?.let { login ->
                it.crmLogin = login
                it.managerLogin = login
                if (withExt) {
                    logger.info { "\uD83D\uDC35 '-ext' суффикс будет добавлен к логину $login \n" }
                     it.managerLogin = "$login-ext"
                }
                logger.info { "Логин заменен: ${it.managerLogin} --> $login\n" }
            }
            it
        }
}