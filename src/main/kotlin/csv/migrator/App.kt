package csv.migrator

import csv.migrator.service.Converter.migrateIserve


@ExperimentalStdlibApi
fun main(args: Array<String>?) {
    migrateIserve(args?.isNotEmpty() == true)
    println("\uD83D\uDC4C OK: все сконвертировано")
}
