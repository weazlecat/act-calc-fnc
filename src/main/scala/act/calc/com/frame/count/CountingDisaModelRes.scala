package act.calc.com.frame.count

import act.calc.com.Attribute.DisaModelRes

implicit object CountingDisaModelRes extends Counting[DisaModelRes]:

  def compare(x: DisaModelRes, y: DisaModelRes): Int = x.ordinal compare y.ordinal

  def fromInt(x: Int): DisaModelRes = DisaModelRes.fromOrdinal(x)

  def parseString(str: String): Option[DisaModelRes] =
    try
      Some(DisaModelRes.valueOf(str))
    catch
      case e: Exception => None
      
  def toInt(x: DisaModelRes): Int = x.ordinal

end CountingDisaModelRes
