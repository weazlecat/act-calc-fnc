package act.calc.fnc.core

import act.calc.com.shortdate.ShortDate
import act.calc.fnc.api.FncConfig
import act.calc.fnc.data.{StockPool, StockRecord}

import scala.collection.mutable.ListBuffer

class Calculator(
  val stockDataBasis: StockPool,
  val projection: Calculator.ProjectionAttrib,
  val dateOfStockFrom: ShortDate,
  val dateOfStockTo: ShortDate,
  val conf: FncConfig.CalcConf
):

  def projection(dateOfProjectionTo: ShortDate, maturity: Int, average: Int): CalcPool =
    val stockDataExtension = extendStockDataBasis(dateOfProjectionTo)
    val stockData = StockPool(stockDataBasis.iterator ++ stockDataExtension.iterator)
    val lb: ListBuffer[CalcRecord] = ListBuffer[CalcRecord]()
    for (dateActual <- ShortDate.range(dateOfStockFrom, dateOfProjectionTo.next))
      lb += CalcRecord(
        date = dateActual.toString,
        yield_rate = round(yieldRate(stockData, dateActual), conf.rounding.yieldRate),
        street_duration = round(streetDuration(stockData, dateActual), conf.rounding.streetDuration),
        swap_rate_at_street_duration = round(swapRateAtStreetDuration(stockData, dateActual), conf.rounding.swapRateAtStreetDuration),
        spread_at_street_duration = round(spreadAtStreetDuration(stockData, dateActual), conf.rounding.spreadAtStreetDuration),
        yield_average = round(yieldRate(stockData, dateActual, average), conf.rounding.yieldRateAverage),
        street_duration_average = round(streetDuration(stockData, dateActual, average), conf.rounding.streetDurationAverage),
        swap_rate_at_street_duration_average = round(swapRateAtStreetDuration(stockData, dateActual, average), conf.rounding.swapRateAtStreetDuration),
        spread_at_street_duration_average = round(round(hgbRate(stockData, dateActual, maturity, average), conf.rounding.hgbRate) - round(swapRate(stockData, dateActual, maturity, average), conf.rounding.swapRate), conf.rounding.spreadAtStreetDurationAverage),
        swap_rate = round(swapRate(stockData, dateActual, maturity), conf.rounding.swapRate),
        swap_rate_average = round(swapRate(stockData, dateActual, maturity, average), conf.rounding.swapRateAverage),
        hgb_rate = round(hgbRate(stockData, dateActual, maturity, average), conf.rounding.hgbRate),
        hgb_rate_delta = round(hgbRateDelta(stockData, dateActual, maturity, average), conf.rounding.hgbRateDelta)
      )
    CalcPool(lb.toList)

  def extendStockDataBasis(dateOfProjectionTo: ShortDate): StockPool =
    val extension: CalculatorExtension = projection match
      case Calculator.ProjectionAttrib.BestEstimate => new CalculatorExtensionBestEstimate(stockDataBasis.select(dateOfStockFrom, dateOfStockTo), conf)
      case Calculator.ProjectionAttrib.Flat => new CalculatorExtensionFlat(stockDataBasis.select(dateOfStockFrom, dateOfStockTo))
    val lb: ListBuffer[StockRecord] = ListBuffer()
    for (dateActual <- ShortDate.range(dateOfStockTo.next, dateOfProjectionTo.next))
      val swap: Array[Double] = new Array(StockRecord.pw-StockRecord.p0+1)
      for (p <- StockRecord.p0 to StockRecord.pw) swap(p) = extension.swapRate(dateActual, p)
      lb += StockRecord(
        date = dateActual.toString,
        yield_rate = extension.yieldRate(dateActual),
        street_duration = extension.streetDuration(dateActual),
        swap = swap)
    StockPool(lb.iterator)

  def yieldRate(stockData: StockPool, date: ShortDate, average: Int = 1): Double =
    if (average==1)
      stockData.select(date).YIELD_RATE
    else
      val dateAv = dateByAverage(date, average)
      if (dataContainsDates(stockData, dateAv, date))
        stockData.select(dateAv, date).meanValue(StockRecord.yieldRateCol)
      else
        Double.NaN

  def streetDuration(stockData: StockPool, date: ShortDate, average: Int = 1): Double =
    if (average==1)
      stockData.select(date).STREET_DURATION
    else
      val dateAv = dateByAverage(date, average)
      if (dataContainsDates(stockData, dateAv, date))
        stockData.select(dateAv, date).meanValue(StockRecord.streetDurationCol)
      else
        Double.NaN

  def swapRate(stockData: StockPool, date: ShortDate, maturity: Int, average: Int = 1): Double =
    if (average==1)
      stockData.select(date).swap(maturity)
    else
      val dateAv = dateByAverage(date, average)
      if (dataContainsDates(stockData, dateAv, date))
        stockData.select(dateAv, date).meanValue(StockRecord.swapCol(maturity))
      else
        Double.NaN

  def swapRateAtStreetDuration(stockData: StockPool, date: ShortDate, average: Int = 1): Double =
    if (dataContainsDates(stockData, dateByAverage(date, average), date))
      val streetDurationValue: Double = streetDuration(stockData, date, average)
      val streetDurationValueMin: Int = streetDurationValue.floor.toInt
      val streetDurationValueMax: Int = streetDurationValue.ceil.toInt
      val swapRateAtStreetDurationMin: Double = swapRate(stockData, date, streetDurationValueMin, average)
      val swapRateAtStreetDurationMax: Double = swapRate(stockData, date, streetDurationValueMax, average)
      swapRateAtStreetDurationMin + (streetDurationValue - streetDurationValueMin) * (swapRateAtStreetDurationMax - swapRateAtStreetDurationMin)
    else
      Double.NaN

  def spreadAtStreetDuration(stockData: StockPool, date: ShortDate, average: Int = 1): Double =
    if (dataContainsDates(stockData, dateByAverage(date, average), date))
      yieldRate(stockData, date, average) - swapRateAtStreetDuration(stockData, date, average)
    else
      Double.NaN

  def hgbRate(stockData: StockPool, date: ShortDate, maturity: Int, average: Int): Double =
    if (dataContainsDates(stockData, dateByAverage(date, average), date))
      spreadAtStreetDuration(stockData, date, average) + swapRate(stockData, date, maturity, average)
    else
      Double.NaN

  def hgbRateDelta(stockData: StockPool, date: ShortDate, maturity: Int, average: Int): Double =
    if (dataContainsDates(stockData, dateByAverage(date, average+12), date))
      hgbRate(stockData, date, maturity, average) - hgbRate(stockData, date - 12, maturity, average)
    else
      Double.NaN

  def round(d: Double, s: Int): Double = if (d.isNaN) d else BigDecimal(d).setScale(s, BigDecimal.RoundingMode.HALF_UP).toDouble

  def dateByAverage(date: ShortDate, average: Int): ShortDate = date - average + 1

  def dataContainsDates(stockData: StockPool, dateA: ShortDate, dateB: ShortDate): Boolean = stockData.contains(dateA) && stockData.contains(dateB)

end Calculator

object Calculator:

  sealed trait ProjectionAttrib
  object ProjectionAttrib:
    case object Flat extends ProjectionAttrib
    case object BestEstimate extends ProjectionAttrib
    def valueOf(s: String): ProjectionAttrib = s match
      case "Flat" => Flat
      case "BestEstimate" => BestEstimate
  end ProjectionAttrib

end Calculator
