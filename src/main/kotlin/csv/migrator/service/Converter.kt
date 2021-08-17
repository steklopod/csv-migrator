package csv.migrator.service

import csv.migrator.model.Compass
import csv.migrator.model.Iserve
import csv.migrator.model.Migration
import org.slf4j.LoggerFactory

@ExperimentalStdlibApi
object Converter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun migrateIserve(): List<List<String?>> {
        println("\uD83C\uDFB1 Выбран режим миграции для iserve")
        val migrationsMap = Reader.readMigrationsMap()
        val iserveUpdated: List<Iserve> = updateIserveLogins(migrationsMap)
        return Writer.writeIserve(iserveUpdated)
    }

    fun migrateCompass(): List<List<String?>> {
        println("\uD83C\uDFB1 Выбран режим EXT: миграция для compass")
        val migrationsMap = Reader.readMigrationsMap()
        val compassUpdated: List<Compass> = updateCompassLogins(migrationsMap)
        return Writer.writeCompass(compassUpdated)
    }

    private fun updateIserveLogins(migrationsMap: Map<String?, Migration>): List<Iserve> {
        val readIserve = Reader.readIserve()
        return readIserve.map {
            migrationsMap[it.managerTubNumber]?.NEW_LOGIN?.let { login ->
                logger.info("Логин заменен: ${it.managerLogin} --> $login\n")
                it.crmLogin = login
                it.managerLogin = login
            }
            it
        }
    }

    private fun updateCompassLogins(migrationsMap: Map<String?, Migration>): List<Compass> {
        val readCompass = Reader.readCompass()
        return readCompass.map {
            migrationsMap[it.managerTubNumber]?.NEW_LOGIN?.let { login ->
                logger.info("Логин заменен: ${it.managerLogin} --> $login-ext \n")
                it.crmLogin = login
                it.managerLogin = "$login-ext"
            }
            it
        }
    }


}
