package act.calc.com.utils

object UtilMath:

  def round(value: Double, scale: Int, mode: BigDecimal.RoundingMode.Value): Any =
    mode match
      case BigDecimal.RoundingMode.UNNECESSARY => value
      case _ => BigDecimal(value).setScale(scale, mode).toDouble

end UtilMath
