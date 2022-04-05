package act.calc.fnc.core

import act.calc.com.shortdate.ShortDate
import act.calc.com.frame.count.CountingShortDate
import act.calc.fnc.data.{StockPool, StockRecord}
import math.Ordered.orderingToOrdered

import scala.collection.immutable

class CalcPool(private val map: immutable.TreeMap[ShortDate,CalcRecord]):

  def select(from: ShortDate, to: ShortDate): CalcPool = new CalcPool(map.filter(x => x._1 >= from && x._1 <= to))
  def select(date: ShortDate): CalcRecord = map(date)
  def first: CalcRecord = map(map.firstKey)

end CalcPool

object CalcPool:

  def apply(li: List[CalcRecord]): CalcPool =
    val builder = immutable.TreeMap.newBuilder[ShortDate,CalcRecord]
    for (elem <- li) do builder += {(ShortDate(elem.date), elem)}
    new CalcPool(builder.result)

end CalcPool