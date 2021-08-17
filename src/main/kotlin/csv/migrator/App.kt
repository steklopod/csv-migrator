package csv.migrator

import csv.migrator.service.Converter.migrateCompass
import csv.migrator.service.Converter.migrateIserve


@ExperimentalStdlibApi
fun main(args: Array<String>?) {
    val withExt = args?.isNotEmpty() == true
    if(withExt) migrateCompass()
    else migrateIserve()
    println("\uD83D\uDC4C OK: все сконвертировано")
}
