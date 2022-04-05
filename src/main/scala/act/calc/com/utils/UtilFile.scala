package act.calc.com.utils

import act.calc.com.FileId
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.{MapperFeature, ObjectReader, ObjectWriter}
import com.fasterxml.jackson.dataformat.csv.{CsvGenerator, CsvMapper, CsvParser, CsvSchema}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import java.io.{File, InputStream}
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters.{IterableHasAsJava, ListHasAsScala}
import scala.reflect.{ClassTag, classTag}

object UtilFile:

  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  // ----- Path of Project
  private val pathNameRuntime: String = URLDecoder.decode(this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath, StandardCharsets.UTF_8)
  logger.info("Runtime Environment - PATH-RUNTIME-PROD    [%s]".format(pathNameRuntime))

  private val pathNameApplication: String = new File(".").getAbsolutePath
  logger.info("Runtime Environment - PATH-APPLICATION-PROD[%s]".format(pathNameApplication))

  // ----- CSV
  private val csvMapperBuilder: CsvMapper.Builder = CsvMapper.builder
  csvMapperBuilder.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
  csvMapperBuilder.configure(CsvParser.Feature.TRIM_SPACES, true)
  csvMapperBuilder.configure(CsvParser.Feature.ALLOW_TRAILING_COMMA, true)
  csvMapperBuilder.configure(CsvParser.Feature.INSERT_NULLS_FOR_MISSING_COLUMNS, true)
  csvMapperBuilder.configure(CsvParser.Feature.SKIP_EMPTY_LINES, true)
  csvMapperBuilder.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, false)
  csvMapperBuilder.configure(CsvGenerator.Feature.STRICT_CHECK_FOR_QUOTING, true)
  private val csvMapper = csvMapperBuilder.build
  csvMapper.registerModule(DefaultScalaModule)

  def csvRead[T: ClassTag](fileId: FileId, delimiter: Char): List[T] =
    val inputStream: InputStream = fileId.inputStream
    val clazz: Class[_] = classTag[T].runtimeClass
    val schema: CsvSchema = csvMapper.schemaFor(clazz).withHeader.withColumnSeparator(delimiter)
    val reader: ObjectReader = csvMapper.readerFor(clazz).`with`(schema)
    val list: List[T] = reader.readValues[T](inputStream).readAll.asScala.toList
    inputStream.close()
    list

  def csvWrite[T: ClassTag](items: List[T], fileId: FileId, delimiter: Char): Unit =
    val clazz: Class[_] = classTag[T].runtimeClass
    val schema: CsvSchema = csvMapper.schemaFor(clazz).withHeader.withColumnSeparator(delimiter)
    val writer: ObjectWriter = csvMapper.writerFor(clazz).`with`(schema)
    writer.writeValues(fileId.file).writeAll(items.asJava)

  // ---- JSON
  private val jsonMapperBuilder: JsonMapper.Builder = JsonMapper.builder
  private val jsonMapper = jsonMapperBuilder.build
  jsonMapper.registerModule(DefaultScalaModule)

  def jsonRead[T: ClassTag](fileId: FileId): T =
    val inputStream: InputStream = fileId.inputStream
    val clazz: Class[_] = classTag[T].runtimeClass
    val reader: ObjectReader = jsonMapper.readerFor(clazz)
    val json: T = reader.readValue(inputStream)
    json

  def removeFileExtension(filename: String, removeAllExtensions: Boolean): String =
    if (filename.isEmpty)
      filename
    else
      val extPattern = "(?<!^)[.]" + (if (removeAllExtensions) ".*" else "[^.]*$")
      filename.replaceAll(extPattern, "")

end UtilFile
