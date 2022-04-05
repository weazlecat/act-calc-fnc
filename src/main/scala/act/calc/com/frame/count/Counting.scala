package act.calc.com.frame.count

trait Counting[T] extends Ordering[T]:

  def compare(x: T, y: T): Int

  def fromInt(x: Int): T

  def parseString(str: String): Option[T]

  def toInt(x: T): Int

end Counting
