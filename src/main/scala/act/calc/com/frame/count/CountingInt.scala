package act.calc.com.frame.count

implicit object CountingInt extends Counting[Int]:

  def compare(x: Int, y: Int): Int = x compare y

  def fromInt(x: Int): Int = x

  def parseString(str: String): Option[Int] =
    try
      Some(str.toInt)
    catch
      case e: Exception => None

  def toInt(x: Int): Int = x

end CountingInt
