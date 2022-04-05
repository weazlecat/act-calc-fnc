package act.calc.com.frame.count

import act.calc.com.Attribute.PopuModel

implicit object CountingPopuModel extends Counting[PopuModel]:

  def compare(x: PopuModel, y: PopuModel): Int = x.ordinal compare y.ordinal

  def fromInt(x: Int): PopuModel = PopuModel.fromOrdinal(x)

  def parseString(str: String): Option[PopuModel] =
    try
      Some(PopuModel.valueOf(str))
    catch
      case e: Exception => None
      
  def toInt(x: PopuModel): Int = x.ordinal

end CountingPopuModel
