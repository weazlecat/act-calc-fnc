package act.calc.com

import act.calc.com.FileIdPath.Location
import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable.ListBuffer

case class FileIdPath(
  filePath: String,
  fileLocation: Location
):

  def createDirectory: Boolean =
    if (Files.exists(this.path))
      false
    else
      Files.createDirectory(this.path)
      true

  def listDirectory: List[String] =
    val inputStream: InputStream = getClass.getClassLoader.getResourceAsStream(filePath)
    val br: BufferedReader = new BufferedReader(new InputStreamReader(inputStream))
    val lines = new ListBuffer[String]()
    var line: String = null
    while ({line = br.readLine; line != null})
      lines += line
    br.close()
    lines.toList

  def path: Path = Paths.get(filePath)

  override def toString: String = "%s|%s".format(filePath, fileLocation)

end FileIdPath

object FileIdPath:

  enum Location:
    case RUN
    case APP
  end Location

end FileIdPath