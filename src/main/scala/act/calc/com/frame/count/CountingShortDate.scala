package act.calc.com.frame.count

import act.calc.com.shortdate.ShortDate

implicit object CountingShortDate extends Counting[ShortDate]:

  def compare(x: ShortDate, y: ShortDate): Int = x.count compare y.count

  def fromInt(x: Int): ShortDate = new ShortDate(x)

  def parseString(str: String): Option[ShortDate] =
    try
      Some(ShortDate(str.split("-")(0).toInt, str.split("-")(1).toInt))
    catch
      case e: Exception => None

  def toInt(x: ShortDate): Int = x.count

end CountingShortDate
