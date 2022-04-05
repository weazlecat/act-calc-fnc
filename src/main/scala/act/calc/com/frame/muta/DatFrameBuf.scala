package act.calc.com.frame.muta

import act.calc.com.frame.count.Counting
import act.calc.com.frame.{DatFrame, immu, muta}

import scala.collection.{SortedSet, immutable, mutable}
import scala.reflect.ClassTag
import math.Ordering.Implicits.infixOrderingOps

class DatFrameBuf[X,Y,V](it: IterableOnce[(X,Y,V)])(implicit val countX: Counting[X], val countY: Counting[Y], val numV: Numeric[V]) extends DatFrame[X,Y,V]:

  def this()(implicit countX: Counting[X], countY: Counting[Y], numV: Numeric[V]) = this(new mutable.ListBuffer[(X,Y,V)].iterator)(countX, countY, numV)

  private val dataMap: mutable.TreeMap[Y,muta.DatVectorBuf[X,V]] = new mutable.TreeMap

  for (dat <- it) do this(dat._1, dat._2) = dat._3

  def apply(x: X, y: Y): V = dataMap(y)(x)

  def column(y: Y): DatVectorBuf[X,V] = dataMap(y)

  def columnsAll: SortedSet[Y] =
    val builder = immutable.TreeSet.newBuilder[Y]
    if !this.isEmpty then
      for y <- countY.toInt(this.firstColumn) to countY.toInt(this.lastColumn) do builder += countY.fromInt(y)
    builder.result

  def columnsContains: SortedSet[Y] = dataMap.keySet

  def firstColumn: Y = dataMap.firstKey

  def firstRow: X =
    var tmpRow: Option[X] = None
    for (y <- columnsContains.iterator) do
      if column(y).nonEmpty then
        if tmpRow.isDefined then
          if column(y).firstIndex < tmpRow.get then tmpRow = Some(column(y).firstIndex) else {}
        else
          tmpRow = Some(column(y).firstIndex)
    tmpRow.get

  def get(x: X, y: Y): Option[V] = if dataMap.contains(y) then dataMap(y).get(x) else None

  def isColumn(y: Y): Boolean = dataMap.contains(y)

  override def isEmpty: Boolean = dataMap.isEmpty

  def isRow(x: X): Boolean =
    var tmpIsRow: Boolean = false
    for (y <- columnsContains.iterator) do if column(y).nonEmpty then tmpIsRow = tmpIsRow || column(y).isIndex(x)
    tmpIsRow

  def iterator: Iterator[(X,Y,V)] =
    val lb = new mutable.ListBuffer[(X,Y,V)]
    for k <- dataMap.iterator do
      for c <- k._2.iterator do lb += {(c._1, k._1, c._2)}
    lb.iterator

  def lastColumn: Y = dataMap.lastKey

  def lastRow: X =
    var tmpRow: Option[X] = None
    for (y <- columnsContains.iterator) do
      if !column(y).isEmpty then
        if tmpRow.isDefined then
          if column(y).firstIndex > tmpRow.get then tmpRow = Some(column(y).firstIndex) else {}
        else
          tmpRow = Some(column(y).firstIndex)
    tmpRow.get

  def row(x: X): DatVectorBuf[Y,V] =
    val tmpRow = new DatVectorBuf[Y,V]
    for (y <- columnsContains.iterator) do
      val v = this.get(x, y)
      if v.isDefined then tmpRow(y) = v.get
    tmpRow

  def rowsAll: SortedSet[X] =
    val builder = immutable.TreeSet.newBuilder[X]
    if !this.isEmpty then
      for x <- countX.toInt(this.firstRow) to countX.toInt(this.lastRow) do builder += countX.fromInt(x)
    builder.result

  def rowsContains: SortedSet[X] =
    val builder = immutable.TreeSet.newBuilder[X]
    if !this.isEmpty then
      for x <- countX.toInt(this.firstRow) to countX.toInt(this.lastRow) do
        val row = countX.fromInt(x)
        if this.isRow(row) then builder += row
    builder.result

  def toDatFrameBuf: muta.DatFrameBuf[X,Y,V] = this

  def toDatFrameFix: immu.DatFrameFix[X,Y,V] = immu.DatFrameFix(this.iterator)

  def update(x: X, y: Y, value: V): Unit =
    if !dataMap.contains(y) then dataMap(y) = new DatVectorBuf[X,V]
    dataMap(y)(x) = value

  def update(y: Y, col: DatVectorBuf[X,V]): Unit =
    if !dataMap.contains(y) then dataMap(y) = new DatVectorBuf[X,V]
    dataMap(y) = col

end DatFrameBuf
