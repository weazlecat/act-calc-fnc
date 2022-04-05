package act.calc.fnc.core

import act.calc.com.shortdate.ShortDate
import act.calc.fnc.data.{StockPool, StockRecord}

class CalculatorExtensionFlat(stock: StockPool) extends CalculatorExtension {

  private val lastStockRecord: StockRecord = stock.last
  private val dateOfStockTo = ShortDate(lastStockRecord.DATE)

  def yieldRate(date: ShortDate): Double =
    if (ShortDate.distance(dateOfStockTo, date)<0) stock.select(date).YIELD_RATE
    else lastStockRecord.YIELD_RATE

  def streetDuration(date: ShortDate): Double =
    if (ShortDate.distance(dateOfStockTo, date)<0) stock.select(date).STREET_DURATION
    else lastStockRecord.STREET_DURATION

  def swapRate(date: ShortDate, maturity: Int): Double =
    if (ShortDate.distance(dateOfStockTo, date)<0) stock.select(date).swap(maturity)
    else lastStockRecord.swap(maturity)

}