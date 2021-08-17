package csv.migrator.service

import com.github.doyaaaaaken.kotlincsv.dsl.context.WriteQuoteMode
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import csv.migrator.model.Compass
import csv.migrator.model.Iserve
import csv.migrator.service.Reader.iserveFile
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.reflect.full.memberProperties


@ExperimentalStdlibApi
object Writer {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private fun randomString() = (1..4).map { i -> kotlin.random.Random.nextInt(0, charPool.size) }.map(charPool::get).joinToString("")


    fun writeIserve(iserveUpdated: List<Iserve>): List<List<String?>> {
        val headers = headers(iserveFile)
        val rows: MutableList<List<String?>> = mutableListOf(headers)
        iserveUpdated.forEach { iserve ->
            val hashMap = iserve.asMap()
            val row = headers.map { hashMap[it]?.toString() }
            rows.add(row)
        }
        val newFileName = "${LocalDate.now()} _${randomString()}_ $iserveFile"
        val newFile = Reader.file(newFileName)
        writerIserve.writeAll(rows, newFile) // TODO
        logger.info("Записано ${rows.size} строк в файл: $newFileName по пути: $newFile \n")
        return rows
    }

    fun writeCompass(compassUpdated: List<Compass>): List<List<String?>> {
        val headers = headers(Reader.compassFile)
        val rows: MutableList<List<String?>> = mutableListOf(headers)
        compassUpdated.forEach { compass ->
            val hashMap = compass.asMap()
            val row = headers.map { hashMap[it]?.toString() }
            rows.add(row)
        }
        val newFileName = "${LocalDate.now()} _${randomString()}_ ${Reader.compassFile}"
        val newFile = Reader.file(newFileName)
        csvWriter().writeAll(rows, newFile) // TODO
        logger.info("Записано ${rows.size} строк в файл: $newFileName по пути: $newFile \n")
        return rows
    }
    private fun headers(file: String): List<String> =
        Reader.file(file).readLines().first().replace("\"", "").let {
           val splitterChar =  if(it.contains(';')) ';' else ','
           it.split(splitterChar)
        }

    private val writerIserve = csvWriter {
        delimiter = ';'
        nullCode = "\"\""
        quote {
            mode = WriteQuoteMode.ALL
            char = '"'
        }
    }

    private inline fun <reified T : Any> T.asMap(): Map<String, Any?> {
        val props = T::class.memberProperties.associateBy { it.name }
        return props.keys.associateWith { props[it]?.get(this) }
    }

}
