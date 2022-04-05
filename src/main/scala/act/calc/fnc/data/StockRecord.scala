package act.calc.fnc.data

case class StockRecord(
  DATE: String = "",
  YIELD_RATE: Double = Double.NaN,
  STREET_DURATION: Double = Double.NaN,
  P000: Double = Double.NaN,
  P001: Double = Double.NaN,
  P002: Double = Double.NaN,
  P003: Double = Double.NaN,
  P004: Double = Double.NaN,
  P005: Double = Double.NaN,
  P006: Double = Double.NaN,
  P007: Double = Double.NaN,
  P008: Double = Double.NaN,
  P009: Double = Double.NaN,
  P010: Double = Double.NaN,
  P011: Double = Double.NaN,
  P012: Double = Double.NaN,
  P013: Double = Double.NaN,
  P014: Double = Double.NaN,
  P015: Double = Double.NaN,
  P016: Double = Double.NaN,
  P017: Double = Double.NaN,
  P018: Double = Double.NaN,
  P019: Double = Double.NaN,
  P020: Double = Double.NaN,
  P021: Double = Double.NaN,
  P022: Double = Double.NaN,
  P023: Double = Double.NaN,
  P024: Double = Double.NaN,
  P025: Double = Double.NaN,
  P026: Double = Double.NaN,
  P027: Double = Double.NaN,
  P028: Double = Double.NaN,
  P029: Double = Double.NaN,
  P030: Double = Double.NaN,
  P031: Double = Double.NaN,
  P032: Double = Double.NaN,
  P033: Double = Double.NaN,
  P034: Double = Double.NaN,
  P035: Double = Double.NaN,
  P036: Double = Double.NaN,
  P037: Double = Double.NaN,
  P038: Double = Double.NaN,
  P039: Double = Double.NaN,
  P040: Double = Double.NaN,
  P041: Double = Double.NaN,
  P042: Double = Double.NaN,
  P043: Double = Double.NaN,
  P044: Double = Double.NaN,
  P045: Double = Double.NaN,
  P046: Double = Double.NaN,
  P047: Double = Double.NaN,
  P048: Double = Double.NaN,
  P049: Double = Double.NaN,
  P050: Double = Double.NaN
) {

  def swap(p:Int): Double = {
    p match {
      case 0 => P000
      case 1 => P001
      case 2 => P002
      case 3 => P003
      case 4 => P004
      case 5 => P005
      case 6 => P006
      case 7 => P007
      case 8 => P008
      case 9 => P009
      case 10 => P010
      case 11 => P011
      case 12 => P012
      case 13 => P013
      case 14 => P014
      case 15 => P015
      case 16 => P016
      case 17 => P017
      case 18 => P018
      case 19 => P019
      case 20 => P020
      case 21 => P021
      case 22 => P022
      case 23 => P023
      case 24 => P024
      case 25 => P025
      case 26 => P026
      case 27 => P027
      case 28 => P028
      case 29 => P029
      case 30 => P030
      case 31 => P031
      case 32 => P032
      case 33 => P033
      case 34 => P034
      case 35 => P035
      case 36 => P036
      case 37 => P037
      case 38 => P038
      case 39 => P039
      case 40 => P040
      case 41 => P041
      case 42 => P042
      case 43 => P043
      case 44 => P044
      case 45 => P045
      case 46 => P046
      case 47 => P047
      case 48 => P048
      case 49 => P049
      case 50 => P050
      case _ => Double.NaN
    }
  }
}

object StockRecord {

  val p0: Int = 0
  val pw: Int = 50

  val dateCol: String = "DATE"
  val yieldRateCol: String = "YIELD_RATE"
  val streetDurationCol: String = "STREET_DURATION"
  val p000Col: String = "P000"
  val p001Col: String = "P001"
  val p002Col: String = "P002"
  val p003Col: String = "P003"
  val p004Col: String = "P004"
  val p005Col: String = "P005"
  val p006Col: String = "P006"
  val p007Col: String = "P007"
  val p008Col: String = "P008"
  val p009Col: String = "P009"
  val p010Col: String = "P010"
  val p011Col: String = "P011"
  val p012Col: String = "P012"
  val p013Col: String = "P013"
  val p014Col: String = "P014"
  val p015Col: String = "P015"
  val p016Col: String = "P016"
  val p017Col: String = "P017"
  val p018Col: String = "P018"
  val p019Col: String = "P019"
  val p020Col: String = "P020"
  val p021Col: String = "P021"
  val p022Col: String = "P022"
  val p023Col: String = "P023"
  val p024Col: String = "P024"
  val p025Col: String = "P025"
  val p026Col: String = "P026"
  val p027Col: String = "P027"
  val p028Col: String = "P028"
  val p029Col: String = "P029"
  val p030Col: String = "P030"
  val p031Col: String = "P031"
  val p032Col: String = "P032"
  val p033Col: String = "P033"
  val p034Col: String = "P034"
  val p035Col: String = "P035"
  val p036Col: String = "P036"
  val p037Col: String = "P037"
  val p038Col: String = "P038"
  val p039Col: String = "P039"
  val p040Col: String = "P040"
  val p041Col: String = "P041"
  val p042Col: String = "P042"
  val p043Col: String = "P043"
  val p044Col: String = "P044"
  val p045Col: String = "P045"
  val p046Col: String = "P046"
  val p047Col: String = "P047"
  val p048Col: String = "P048"
  val p049Col: String = "P049"
  val p050Col: String = "P050"
  def swapCol(i: Int): String = "P%03d".format(i)

  def apply(
    date: String,
    yield_rate: Double,
    street_duration: Double,
    p000: Double,
    p001: Double,
    p002: Double,
    p003: Double,
    p004: Double,
    p005: Double,
    p006: Double,
    p007: Double,
    p008: Double,
    p009: Double,
    p010: Double,
    p011: Double,
    p012: Double,
    p013: Double,
    p014: Double,
    p015: Double,
    p016: Double,
    p017: Double,
    p018: Double,
    p019: Double,
    p020: Double,
    p021: Double,
    p022: Double,
    p023: Double,
    p024: Double,
    p025: Double,
    p026: Double,
    p027: Double,
    p028: Double,
    p029: Double,
    p030: Double,
    p031: Double,
    p032: Double,
    p033: Double,
    p034: Double,
    p035: Double,
    p036: Double,
    p037: Double,
    p038: Double,
    p039: Double,
    p040: Double,
    p041: Double,
    p042: Double,
    p043: Double,
    p044: Double,
    p045: Double,
    p046: Double,
    p047: Double,
    p048: Double,
    p049: Double,
    p050: Double
  ): StockRecord = new StockRecord(
    date,
    yield_rate,
    street_duration,
    p000,
    p001,
    p002,
    p003,
    p004,
    p005,
    p006,
    p007,
    p008,
    p009,
    p010,
    p011,
    p012,
    p013,
    p014,
    p015,
    p016,
    p017,
    p018,
    p019,
    p020,
    p021,
    p022,
    p023,
    p024,
    p025,
    p026,
    p027,
    p028,
    p029,
    p030,
    p031,
    p032,
    p033,
    p034,
    p035,
    p036,
    p037,
    p038,
    p039,
    p040,
    p041,
    p042,
    p043,
    p044,
    p045,
    p046,
    p047,
    p048,
    p049,
    p050
  )

  def apply(
    date: String,
    yield_rate: Double,
    street_duration: Double,
    swap: Array[Double]
  ): StockRecord = new StockRecord(
    date,
    yield_rate,
    street_duration,
    swap(0),
    swap(1),
    swap(2),
    swap(3),
    swap(4),
    swap(5),
    swap(6),
    swap(7),
    swap(8),
    swap(9),
    swap(10),
    swap(11),
    swap(12),
    swap(13),
    swap(14),
    swap(15),
    swap(16),
    swap(17),
    swap(18),
    swap(19),
    swap(20),
    swap(21),
    swap(22),
    swap(23),
    swap(24),
    swap(25),
    swap(26),
    swap(27),
    swap(28),
    swap(29),
    swap(30),
    swap(31),
    swap(32),
    swap(33),
    swap(34),
    swap(35),
    swap(36),
    swap(37),
    swap(38),
    swap(39),
    swap(40),
    swap(41),
    swap(42),
    swap(43),
    swap(44),
    swap(45),
    swap(46),
    swap(47),
    swap(48),
    swap(49),
    swap(50)
  )

}