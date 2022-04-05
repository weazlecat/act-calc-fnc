package act.calc.com

import act.calc.com.FileIdPath.Location.{APP, RUN}

import java.io.{File, FileInputStream, FileOutputStream, InputStream, OutputStream}
import java.nio.file.Paths

case class FileId(
  name: String,
  path: FileIdPath
):

  def file: File = path.fileLocation match
    case RUN => throw new Exception("File can not access Runtime Path - FILE_ID[%s]".format(this.toString))
    case APP => new File(Paths.get(path.filePath, name).toString)

  def inputStream: InputStream = path.fileLocation match
    case RUN => getClass.getClassLoader.getResourceAsStream(Paths.get(path.filePath, name).toString)
    case APP => new FileInputStream(this.file)

  def outputStream: OutputStream = path.fileLocation match
    case RUN => throw new Exception("OutputStream can not access Runtime Path - FILE_ID[%s]".format(this.toString))
    case APP => new FileOutputStream(this.file)

  def copyTo(fileId: FileId): Unit =
    this.inputStream.transferTo(fileId.outputStream)

  override def toString: String =
    "%s|%s".format(name, path.toString)
    
end FileId
