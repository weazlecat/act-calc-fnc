package act.calc.fnc.data

import act.calc.com.shortdate.ShortDate
import act.calc.com.frame.count.CountingShortDate
import math.Ordering.Implicits.infixOrderingOps
import scala.collection.mutable
import scala.collection.immutable

class StockPool(private val it: Iterator[StockRecord]):

  private val map: immutable.TreeMap[ShortDate,StockRecord] =
    val builder = immutable.TreeMap.newBuilder[ShortDate,StockRecord]
    for (elem <- it) do builder += {(ShortDate(elem.DATE), elem)}
    builder.result

  private val indexColumn: mutable.HashMap[String,Array[Double]] = mutable.HashMap()

  def select(from: ShortDate, to: ShortDate): StockPool = new StockPool(map.filter(x => x._1 >= from && x._1 <= to).valuesIterator)

  def select(date: ShortDate): StockRecord = map(date)

  def column(columnName: String): Array[Double] =
    if !indexColumn.contains(columnName) then indexColumn(columnName) = columnBuild(columnName)
    indexColumn(columnName)

  private def columnBuild(columnName: String): Array[Double] = columnName match
    case StockRecord.streetDurationCol => map.values.toList.map{x => x.STREET_DURATION}.toArray
    case StockRecord.yieldRateCol => map.values.toList.map{x => x.YIELD_RATE}.toArray
    case StockRecord.p000Col => map.values.toList.map{x => x.P000}.toArray
    case StockRecord.p001Col => map.values.toList.map{x => x.P001}.toArray
    case StockRecord.p002Col => map.values.toList.map{x => x.P002}.toArray
    case StockRecord.p003Col => map.values.toList.map{x => x.P003}.toArray
    case StockRecord.p004Col => map.values.toList.map{x => x.P004}.toArray
    case StockRecord.p005Col => map.values.toList.map{x => x.P005}.toArray
    case StockRecord.p006Col => map.values.toList.map{x => x.P006}.toArray
    case StockRecord.p007Col => map.values.toList.map{x => x.P007}.toArray
    case StockRecord.p008Col => map.values.toList.map{x => x.P008}.toArray
    case StockRecord.p009Col => map.values.toList.map{x => x.P009}.toArray
    case StockRecord.p010Col => map.values.toList.map{x => x.P010}.toArray
    case StockRecord.p011Col => map.values.toList.map{x => x.P011}.toArray
    case StockRecord.p012Col => map.values.toList.map{x => x.P012}.toArray
    case StockRecord.p013Col => map.values.toList.map{x => x.P013}.toArray
    case StockRecord.p014Col => map.values.toList.map{x => x.P014}.toArray
    case StockRecord.p015Col => map.values.toList.map{x => x.P015}.toArray
    case StockRecord.p016Col => map.values.toList.map{x => x.P016}.toArray
    case StockRecord.p017Col => map.values.toList.map{x => x.P017}.toArray
    case StockRecord.p018Col => map.values.toList.map{x => x.P018}.toArray
    case StockRecord.p019Col => map.values.toList.map{x => x.P019}.toArray
    case StockRecord.p020Col => map.values.toList.map{x => x.P020}.toArray
    case StockRecord.p021Col => map.values.toList.map{x => x.P021}.toArray
    case StockRecord.p022Col => map.values.toList.map{x => x.P022}.toArray
    case StockRecord.p023Col => map.values.toList.map{x => x.P023}.toArray
    case StockRecord.p024Col => map.values.toList.map{x => x.P024}.toArray
    case StockRecord.p025Col => map.values.toList.map{x => x.P025}.toArray
    case StockRecord.p026Col => map.values.toList.map{x => x.P026}.toArray
    case StockRecord.p027Col => map.values.toList.map{x => x.P027}.toArray
    case StockRecord.p028Col => map.values.toList.map{x => x.P028}.toArray
    case StockRecord.p029Col => map.values.toList.map{x => x.P029}.toArray
    case StockRecord.p030Col => map.values.toList.map{x => x.P030}.toArray
    case StockRecord.p031Col => map.values.toList.map{x => x.P031}.toArray
    case StockRecord.p032Col => map.values.toList.map{x => x.P032}.toArray
    case StockRecord.p033Col => map.values.toList.map{x => x.P033}.toArray
    case StockRecord.p034Col => map.values.toList.map{x => x.P034}.toArray
    case StockRecord.p035Col => map.values.toList.map{x => x.P035}.toArray
    case StockRecord.p036Col => map.values.toList.map{x => x.P036}.toArray
    case StockRecord.p037Col => map.values.toList.map{x => x.P037}.toArray
    case StockRecord.p038Col => map.values.toList.map{x => x.P038}.toArray
    case StockRecord.p039Col => map.values.toList.map{x => x.P039}.toArray
    case StockRecord.p040Col => map.values.toList.map{x => x.P040}.toArray
    case StockRecord.p041Col => map.values.toList.map{x => x.P041}.toArray
    case StockRecord.p042Col => map.values.toList.map{x => x.P042}.toArray
    case StockRecord.p043Col => map.values.toList.map{x => x.P043}.toArray
    case StockRecord.p044Col => map.values.toList.map{x => x.P044}.toArray
    case StockRecord.p045Col => map.values.toList.map{x => x.P045}.toArray
    case StockRecord.p046Col => map.values.toList.map{x => x.P046}.toArray
    case StockRecord.p047Col => map.values.toList.map{x => x.P047}.toArray
    case StockRecord.p048Col => map.values.toList.map{x => x.P048}.toArray
    case StockRecord.p049Col => map.values.toList.map{x => x.P049}.toArray
    case StockRecord.p050Col => map.values.toList.map{x => x.P050}.toArray

  def isEmpty: Boolean = map.isEmpty

  def first: StockRecord = map(map.firstKey)

  def last: StockRecord = map(map.lastKey)

  def contains(date: ShortDate): Boolean = map.contains(date)

  def meanValue(columnName: String): Double =
    val values: Array[Double] = column(columnName)
    values.sum / values.length

  def count: Long = map.size

  def iterator: Iterator[StockRecord] = map.valuesIterator

end StockPool
