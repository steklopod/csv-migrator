package csv.migrator.service

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import csv.migrator.fileCompass
import csv.migrator.fileIserve
import csv.migrator.fileMigration
import csv.migrator.model.Compass
import csv.migrator.model.Iserve
import csv.migrator.model.Migration
import io.blackmo18.kotlin.grass.dsl.grass
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths
import java.time.LocalDate

@ExperimentalStdlibApi
object Reader {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private const val folder = "/src/main/resources"


    fun file(fileName: String): File {
        val root = Paths.get("").toAbsolutePath().toString()
        val resourcesPath = Paths.get(root, folder).toString()

        val fileInResources = Paths.get(resourcesPath, fileName).toFile()

        val file = fileInResources.let {
            if (it.exists()) it else Paths.get(root, fileName).toAbsolutePath().toFile()
        }
        if (!fileName.contains("${LocalDate.now()}")) logger.info("Поиск файла: $fileName по пути: $file")
        return file
    }

    fun readCompass(): List<Compass> {
        val fileCompass = file(fileCompass)
        val rowsCompass = csvReader().readAllWithHeader(fileCompass)
        val compass = grass<Compass>().harvest(rowsCompass)
        logger.info("Найдено ${compass.size} строк COMPASS в файле: ${csv.migrator.fileCompass} \n")
        return compass
    }

    fun readIserve(): List<Iserve> {
        val fileIserve = file(fileIserve)
        val iserveReader = csvReader { quoteChar = '"'; delimiter = ';' }
        val rowsIserve = iserveReader.readAllWithHeader(fileIserve)
        val iserve = grass<Iserve>().harvest(rowsIserve)
        logger.info("Найдено ${iserve.size} строк ISERVE в файле: ${csv.migrator.fileIserve} \n")
        return iserve
    }

    fun readMigrationsMap(): Map<String?, Migration> = readMigrations().associateBy { it.TAB_NUMBER }
    fun readMigrations(): List<Migration> {
        val fileMigration = file(fileMigration)
        val migrationReader = csvReader { delimiter = ';' }
        val rowsMigration = migrationReader.readAllWithHeader(fileMigration)
        val migrations = grass<Migration>().harvest(rowsMigration)
        logger.info("Найдено ${migrations.size} строк МИГРАЦИЙ в файле: ${csv.migrator.fileMigration} \n")
        return migrations
    }

}
