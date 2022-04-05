package act.calc.com.frame.count

import act.calc.com.Attribute.MortModel

implicit object CountingMortModel extends Counting[MortModel]:

  def compare(x: MortModel, y: MortModel): Int = x.ordinal compare y.ordinal

  def fromInt(x: Int): MortModel = MortModel.fromOrdinal(x)

  def parseString(str: String): Option[MortModel] =
    try
      Some(MortModel.valueOf(str))
    catch
      case e: Exception => None
      
  def toInt(x: MortModel): Int = x.ordinal

end CountingMortModel
