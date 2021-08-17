package csv.migrator

import csv.migrator.service.Converter.migrateCompass
import csv.migrator.service.Converter.migrateIserve


@ExperimentalStdlibApi
fun main(args: Array<String>?) {
    val hasAnyArg = args?.isNotEmpty() == true
    if(hasAnyArg) migrateCompass()
    else migrateIserve()
    println("\uD83D\uDC4C OK: все сконвертировано")
}

const val fileIserve    = "MVI iServe.csv"
const val fileCompass   = "MVI Compass.csv"
const val fileMigration = "МИГРАЦИЯ.csv"