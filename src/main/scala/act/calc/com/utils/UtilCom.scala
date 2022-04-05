package act.calc.com.utils

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

object UtilCom:

  def deepCopy[T](source: T): T =
    val bos: ByteArrayOutputStream = new ByteArrayOutputStream
    val oos: ObjectOutputStream = new ObjectOutputStream(bos)
    oos.writeObject(source)
    oos.flush()
    oos.close()
    bos.close()
    val byteData: Array[Byte] = bos.toByteArray
    val bais: ByteArrayInputStream = new ByteArrayInputStream(byteData)
    new ObjectInputStream(bais).readObject.asInstanceOf[T]

end UtilCom
