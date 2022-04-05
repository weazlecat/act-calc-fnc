package act.calc.com.frame.immu

import act.calc.com.frame.count.Counting
import act.calc.com.frame.{DatFrame, immu, muta}

import scala.collection.{SortedSet, immutable, mutable}
import math.Ordering.Implicits.infixOrderingOps

class DatFrameFix[X,Y,V](it: IterableOnce[(X,Y,V)])(implicit val countX: Counting[X], val countY: Counting[Y], val numV: Numeric[V]) extends DatFrame[X,Y,V]:

  def this()(implicit countX: Counting[X], countY: Counting[Y], numV: Numeric[V]) = this(new mutable.ListBuffer[(X,Y,V)].iterator)(countX, countY,numV)
  
  private val datMap: immutable.TreeMap[Y,immu.DatVectorFix[X,V]] =
    val map = new mutable.TreeMap[Y,muta.DatVectorBuf[X,V]]
    val builder = immutable.TreeMap.newBuilder[Y,immu.DatVectorFix[X,V]]
    for dat <- it do
      if !map.contains(dat._2) then map(dat._2) = new muta.DatVectorBuf[X,V]
      map(dat._2)(dat._1) = dat._3
    for  dat <- map.iterator do
      builder += {(dat._1, dat._2.toDatVectorFix)}
    builder.result()

  private val _firstColumn: Option[Y] = if datMap.isEmpty then None else Some(datMap.firstKey)

  private val _lastColumn: Option[Y] = if datMap.isEmpty then None else Some(datMap.lastKey)

  private val _columnsAll: SortedSet[Y] =
    val builder = immutable.TreeSet.newBuilder[Y]
    if datMap.nonEmpty then
      for y <- countY.toInt(_firstColumn.get) to countY.toInt(_lastColumn.get) do builder += countY.fromInt(y)
    builder.result

  private val _columnsContains: SortedSet[Y] = datMap.keySet

  private val _firstRow: Option[X] =
    if datMap.isEmpty then
      None
    else
      var tmpRow: Option[X] = None
      for (y <- _columnsContains.iterator) do
        if column(y).nonEmpty then
          if tmpRow.isDefined then
            if column(y).firstIndex < tmpRow.get then tmpRow = Some(column(y).firstIndex) else {}
          else
            tmpRow = Some(column(y).firstIndex)
      Some(tmpRow.get)

  private val _lastRow: Option[X] =
    if datMap.isEmpty then
      None
    else
      var tmpRow: Option[X] = None
      for (y <- _columnsContains.iterator) do
        if column(y).nonEmpty then
          if tmpRow.isDefined then
            if column(y).lastIndex > tmpRow.get then tmpRow = Some(column(y).lastIndex) else {}
          else
            tmpRow = Some(column(y).lastIndex)
      Some(tmpRow.get)

  private val _rowsAll: SortedSet[X] =
    val builder = immutable.TreeSet.newBuilder[X]
    if datMap.nonEmpty then
      for x <- countX.toInt(_firstRow.get) to countX.toInt(_lastRow.get) do builder += countX.fromInt(x)
    builder.result

  private val _rowsContains: SortedSet[X] =
    val builder = immutable.TreeSet.newBuilder[X]
    if !this.isEmpty then
      for r <- countX.toInt(_firstRow.get) to countX.toInt(_lastRow.get) do
        val x = countX.fromInt(r)
        var tmpIsRow: Boolean = false
        for (y <- _columnsContains.iterator) do if column(y).nonEmpty then tmpIsRow = tmpIsRow || column(y).isIndex(x)
        if tmpIsRow then builder += x
    builder.result

  def apply(x: X, y: Y): V = datMap(y)(x)

  def column(y: Y): DatVectorFix[X,V] = datMap(y)

  def columnsAll: SortedSet[Y] = _columnsAll

  def columnsContains: SortedSet[Y] = _columnsContains

  def firstColumn: Y = _firstColumn.get

  def firstRow: X = _firstRow.get

  def get(x: X, y: Y): Option[V] = if datMap.contains(y) then datMap(y).get(x) else None

  def isColumn(y: Y): Boolean = _columnsContains.contains(y)

  override def isEmpty: Boolean = datMap.isEmpty

  def isRow(x: X): Boolean = _rowsContains.contains(x)

  def iterator: Iterator[(X,Y,V)] =
    val lb = new mutable.ListBuffer[(X,Y,V)]
    for k <- datMap.iterator do
      for c <- k._2.iterator do lb += {(c._1, k._1, c._2)}
    lb.iterator
  
  def lastColumn: Y = _lastColumn.get

  def lastRow: X = _lastRow.get

  def row(x: X): DatVectorFix[Y,V] =
    val tmpRow = new muta.DatVectorBuf[Y,V]
    for (y <- columnsContains.iterator) do
      val v = this.get(x, y)
      if v.isDefined then tmpRow(y) = v.get
    tmpRow.toDatVectorFix

  def rowsAll: SortedSet[X] = _rowsAll

  def rowsContains: SortedSet[X] = _rowsContains

  def toDatFrameBuf: muta.DatFrameBuf[X,Y,V] = muta.DatFrameBuf(this.iterator)

  def toDatFrameFix: immu.DatFrameFix[X,Y,V] = this
  
end DatFrameFix
