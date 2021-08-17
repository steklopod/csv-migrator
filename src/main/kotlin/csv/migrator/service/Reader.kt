package csv.migrator.service

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import csv.migrator.model.Compass
import csv.migrator.model.Iserve
import csv.migrator.model.Migration
import io.blackmo18.kotlin.grass.dsl.grass
import mu.KotlinLogging
import java.io.File
import java.nio.file.Paths

@ExperimentalStdlibApi
object Reader {
    private val logger = KotlinLogging.logger {}

    private const val folder = "/src/main/resources"

    const val iserveFile = "MVI iServe.csv"
    const val compassFile = "MVI Compass.csv"
    const val migrationFile = "МИГРАЦИЯ.csv"

    fun file(fileName: String): File {
        val root = Paths.get("").toAbsolutePath().toString()
        val resourcesPath = Paths.get(root, folder).toString()

        val fileInResources = Paths.get(resourcesPath, fileName).toFile()

        val file = fileInResources.let {
            if (it.exists()) it else Paths.get(root, fileName).toAbsolutePath().toFile()
        }
        logger.info { "Поиск файла: $fileName по пути, $file \n" }
        return file
    }

    fun readCompassMap(): Map<String?, Compass> = readCompass().associateBy { it.managerTubNumber }
    fun readCompass(): List<Compass> {
        val fileCompass = file(compassFile)
        val rowsCompass = csvReader().readAllWithHeader(fileCompass)
        val compass = grass<Compass>().harvest(rowsCompass)
        logger.info { "Найдено ${compass.size} строк COMPASS в файле: $compassFile \n" }
        return compass
    }

    fun readIserveMap(): Map<String?, Iserve> = readIserve().associateBy { it.managerTubNumber }
    fun readIserve(): List<Iserve> {
        val fileIserve = file(iserveFile)
        val iserveReader = csvReader { quoteChar = '"'; delimiter = ';' }
        val rowsIserve = iserveReader.readAllWithHeader(fileIserve)
        val iserve = grass<Iserve>().harvest(rowsIserve)
        logger.info { "Найдено ${iserve.size} строк ISERVE в файле: $iserveFile \n" }
        return iserve
    }

    fun readMigrationsMap(): Map<String?, Migration> = readMigrations().associateBy { it.TAB_NUMBER }
    fun readMigrations(): List<Migration> {
        val fileMigration = file(migrationFile)
        val migrationReader = csvReader { delimiter = ';' }
        val rowsMigration = migrationReader.readAllWithHeader(fileMigration)
        val migrations = grass<Migration>().harvest(rowsMigration)
        logger.info { "Найдено ${migrations.size} строк МИГРАЦИЙ в файле: $migrationFile \n" }
        return migrations
    }

}