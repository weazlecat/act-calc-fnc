package act.calc.com

import com.typesafe.scalalogging.Logger
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.{ImageType, PDFRenderer}
import org.slf4j.LoggerFactory

import java.io.{BufferedInputStream, ByteArrayInputStream, ByteArrayOutputStream, InputStream}
import java.security.MessageDigest
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex

class Checksum(fileId: FileId):

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  logger.info("Calculating Checksum FILEID[%s]".format(fileId))

  private val regEx: Regex = """.*\.(\w+)""".r
  private val fileExt: String = fileId.name match
    case regEx(ext) => s"$ext"
    case _ => ""
    
  private val inputStream: InputStream = fileId.inputStream
  private val checkArray: Array[Byte] = fileExt.toUpperCase match
    case "PDF" => checksumPdf(inputStream)
    case _ => checksumNoPdf(inputStream)
  
  private val checkString: String = checkArray.map("%02X" format _).mkString

  private def checksumPdf(inputStream: InputStream): Array[Byte] =
    val document = PDDocument.load(inputStream)
    val pdfRenderer = new PDFRenderer(document)
    var result: ArrayBuffer[Byte] = new ArrayBuffer
    for (page <- 0 until document.getNumberOfPages)
      val bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB)
      val byteArrayOutputStream = new ByteArrayOutputStream
      ImageIO.write(bufferedImage, "png", byteArrayOutputStream)
      val bytes: Array[Byte] = byteArrayOutputStream.toByteArray
      val checksum = checksumNoPdf(new ByteArrayInputStream(bytes))
      result = result ++ checksum
    document.close
    result.toArray

  private def checksumNoPdf(inputStream: InputStream): Array[Byte] =
    val md = MessageDigest.getInstance("MD5")
    val bufferedInputStream = new BufferedInputStream(inputStream)
    val buffer = new Array[Byte](8192)
    var count = bufferedInputStream.read(buffer)
    while (count > 0)
      md.update(buffer)
      count = bufferedInputStream.read(buffer)
    bufferedInputStream.close
    md.digest

  def canEqual(a: Any): Boolean = a.isInstanceOf[Checksum]

  override def equals(that: Any): Boolean =
    that match
      case that: Checksum => that.canEqual(this) && this.toString == that.toString
      case _ => false

  override def hashCode: Int = toString.hashCode

  override def toString: String = checkString

end Checksum
