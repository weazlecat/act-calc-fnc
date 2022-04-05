package act.calc.fnc.data

import act.calc.com.FileId
import act.calc.com.utils.UtilFile
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Path

class DataService(fileId: FileId, delimiter: Char):

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  logger.debug("Getting Resource - CSV - BEGIN FILENAME[%s] FILEPATH[%s] FILELOCATION[%s]".format(fileId.name,  fileId.path.filePath, fileId.path.fileLocation))

  private val list: List[StockRecord] = UtilFile.csvRead[StockRecord](fileId, delimiter)

  logger.info("Getting Resource - CSV - END")

  val data: StockPool = new StockPool(list.iterator)
  
end DataService
