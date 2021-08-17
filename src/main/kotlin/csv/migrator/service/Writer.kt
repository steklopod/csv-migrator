package csv.migrator.service

import com.github.doyaaaaaken.kotlincsv.dsl.context.WriteQuoteMode
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import csv.migrator.fileCompass
import csv.migrator.fileIserve
import csv.migrator.model.Manager
import csv.migrator.model.Compass
import csv.migrator.model.Iserve
import csv.migrator.service.Reader.file
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.random.Random.Default.nextInt
import kotlin.reflect.full.memberProperties


@ExperimentalStdlibApi
object Writer {

    fun writeIserve(iserveUpdated: List<Iserve>): List<List<String?>> {
        val rows: MutableList<List<String?>> = rows(iserveUpdated, fileIserve)
        val newFileName = newFileName( fileIserve )
        writerIserve.writeAll(rows, file(newFileName))
        logger.info("Записано ${rows.size} строк в файл: $newFileName\n")
        return rows
    }

    fun writeCompass(compassUpdated: List<Compass>): List<List<String?>> {
        val rows: MutableList<List<String?>> = rows(compassUpdated, fileCompass)
        val newFileName = newFileName( fileCompass )
        csvWriter().writeAll(rows, file(newFileName))
        logger.info("Записано ${rows.size} строк в файл: $newFileName \n")
        return rows
    }

    private fun newFileName(forCsvFile:String): String = "${LocalDate.now()} _${randomString()}_ $forCsvFile"

    private val writerIserve = csvWriter {
        delimiter = ';'
        nullCode = "\"\""
        quote {
            mode = WriteQuoteMode.ALL
            char = '"'
        }
    }

    private inline fun<reified T: Manager> rows(compassUpdated: List<T>, csvFile: String): MutableList<List<String?>> {
        val headers = headers(csvFile)
        val rows: MutableList<List<String?>> = mutableListOf(headers)
        compassUpdated.forEach {
            val hashMap = it.asMap()
            val row = headers.map { h -> hashMap[h]?.toString() }
            rows.add(row)
        }
        return rows
    }

    private fun headers(file: String): List<String> = file(file).readLines().first().replace("\"", "").let {
           val splitterChar =  if(it.contains(';')) ';' else ','
           it.split(splitterChar)
        }

    private inline fun <reified T : Any> T.asMap(): Map<String, Any?> {
        val props = T::class.memberProperties.associateBy { it.name }
        return props.keys.associateWith { props[it]?.get(this) }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private fun randomString() = (1..4).map { nextInt(0, charPool.size) }.map(charPool::get).joinToString("")

}
