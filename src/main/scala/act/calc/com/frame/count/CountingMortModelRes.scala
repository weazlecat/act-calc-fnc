package act.calc.com.frame.count

import act.calc.com.Attribute.MortModelRes

implicit object CountingMortModelRes extends Counting[MortModelRes]:

  def compare(x: MortModelRes, y: MortModelRes): Int = x.ordinal compare y.ordinal

  def fromInt(x: Int): MortModelRes = MortModelRes.fromOrdinal(x)

  def parseString(str: String): Option[MortModelRes] =
    try
      Some(MortModelRes.valueOf(str))
    catch
      case e: Exception => None
      
  def toInt(x: MortModelRes): Int = x.ordinal

end CountingMortModelRes
