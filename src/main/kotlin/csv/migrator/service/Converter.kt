package csv.migrator.service

import csv.migrator.model.Compass
import csv.migrator.model.Iserve
import csv.migrator.model.Migration
import org.slf4j.LoggerFactory

@ExperimentalStdlibApi
object Converter {
    fun migrateIserve(): List<List<String?>> {
        logger.info("\uD83C\uDFB1 Выбран режим миграции для iserve\n")
        val migrationsMap = Reader.readMigrationsMap()
        val iserveUpdated: List<Iserve> = updateIserveLogins(migrationsMap)
        return Writer.writeIserve(iserveUpdated)
    }

    fun migrateCompass(): List<List<String?>> {
        logger.info("\uD83C\uDFB1 Выбран режим EXT: миграция для compass\n")
        val migrationsMap = Reader.readMigrationsMap()
        val compassUpdated: List<Compass> = updateCompassLogins(migrationsMap)
        return Writer.writeCompass(compassUpdated)
    }

    private fun updateIserveLogins(migrationsMap: Map<String?, Migration>): List<Iserve> {
        val readIserve = Reader.readIserve()
        var counter = 0
        val iserveManagers = readIserve.map {
            migrationsMap[it.managerTubNumber]?.NEW_LOGIN?.let { login ->
                counter++
                logger.info("\uD83D\uDFE6 Найдена замена! Логин заменен: ${it.managerLogin} --> $login")
                it.crmLogin = login
                it.managerLogin = login
            }
            it
        }
        logger.info("\n\t♨️ Заменено [$counter] строк ")
        return iserveManagers
    }

    private fun updateCompassLogins(migrationsMap: Map<String?, Migration>): List<Compass> {
        val readCompass = Reader.readCompass()
        var counter = 0
        val compassManagers = readCompass.map {
            migrationsMap[it.managerTubNumber]?.NEW_LOGIN?.let { login ->
                counter++
                logger.info("\uD83D\uDFE6 Найдена `-ext` замена! Логин заменен: ${it.managerLogin} --> $login-ext")
                it.crmLogin = login
                it.managerLogin = "$login-ext"
            }
            it
        }
        logger.info("\n\t♨️ Заменено [$counter] строк ")
        return compassManagers
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

}
