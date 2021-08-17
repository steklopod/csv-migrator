package csv.migrator.service

import com.github.doyaaaaaken.kotlincsv.dsl.context.WriteQuoteMode
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import csv.migrator.model.Iserve
import csv.migrator.service.Reader.iserveFile
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.reflect.full.memberProperties


@ExperimentalStdlibApi
object Writer {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun writeIserve(iserveUpdated: List<Iserve>): List<List<String?>> {
        val fileIserve = Reader.file(iserveFile)

        val headers = fileIserve.readLines().first().replace("\"", "").split(';')

        val rows: MutableList<List<String?>> = mutableListOf(headers)

        iserveUpdated.forEach { iserve ->
            val hashMap = iserve.asMap()
            val row = headers.map { hashMap[it]?.toString() }
            rows.add(row)
        }

        val newFileName = "${LocalDate.now()} $iserveFile"
        val newFile = Reader.file(newFileName)
        writer.writeAll(rows, newFile) // TODO

        logger.info("Записано ${rows.size} строк в файл: $newFileName по пути: $newFile \n")
        return rows
    }

    private val writer = csvWriter {
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
