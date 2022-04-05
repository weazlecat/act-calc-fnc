package act.calc.com.frame.muta

import act.calc.com.frame.count.Counting
import act.calc.com.frame.{DatVector, immu, muta}
import act.calc.com.shortdate.ShortDate
import act.calc.com.utils.UtilMath

import math.Numeric.Implicits.infixNumericOps
import math.Ordering.Implicits.infixOrderingOps
import scala.collection.{SortedSet, immutable, mutable}
import scala.reflect.ClassTag

class DatVectorBuf[X,V](it: IterableOnce[(X,V)])(implicit val counting: Counting[X], val numeric: Numeric[V]) extends DatVector[X,V]:

  def this()(implicit countX: Counting[X], numeric: Numeric[V]) = this(new mutable.ListBuffer[(X,V)].iterator)(countX, numeric)

  private val datMap: mutable.TreeMap[X,V] =
    val builder = mutable.TreeMap.newBuilder[X,V]
    for (elem <- it) builder += elem._1 -> elem._2
    builder.result

  def apply(x: X): V = datMap(x)

  def contains(x: X): Boolean = datMap.contains(x)

  def firstIndex: X = datMap.firstKey

  def firstValue: V = datMap.head._2

  def get(x: X): Option[V] = datMap.get(x)

  def indicesAll: SortedSet[X] =
    val builder = immutable.TreeSet.newBuilder[X]
    for x <- counting.toInt(this.firstIndex) to counting.toInt(this.lastIndex) do builder += counting.fromInt(x)
    builder.result

  def indicesContains: SortedSet[X] = datMap.keySet

  override def isEmpty: Boolean = datMap.isEmpty

  def isIndex(x: X): Boolean = !this.isEmpty && x >= this.firstIndex && x <= this.lastIndex

  def iterator: Iterator[(X,V)] = datMap.iterator

  def lastIndex: X = datMap.lastKey

  def lastValue: V = datMap.last._2

  def range(from: X, until: X): DatVectorBuf[X,V] = DatVectorBuf[X,V](datMap.range(from, until).iterator)

  def rangeFrom(from: X): DatVectorBuf[X,V] = DatVectorBuf[X,V](datMap.rangeFrom(from).iterator)

  def rangeTo(to: X): DatVectorBuf[X,V] = DatVectorBuf[X,V](datMap.rangeTo(to).iterator)

  def rangeUntil(until: X): DatVectorBuf[X,V] = DatVectorBuf[X,V](datMap.rangeUntil(until).iterator)

  def round(scale: Int, mode: BigDecimal.RoundingMode.Value): DatVectorBuf[X,V] =
    val tmpList: mutable.ListBuffer[(X,V)] = mutable.ListBuffer()
    val it = this.iterator
    for (item <- it)
      val e: X = item._1
      val v: V = item._2 match
        case d: Double => UtilMath.round(d, scale, mode).asInstanceOf[V]
        case f: Float => UtilMath.round(f, scale, mode).asInstanceOf[V]
        case _ => item._2
      tmpList += ((e, v))
    DatVectorBuf[X,V](tmpList.iterator)

  def round(scale: Option[Int], mode: BigDecimal.RoundingMode.Value): DatVectorBuf[X,V] = mode match
    case BigDecimal.RoundingMode.UNNECESSARY => this
    case _ => this.round(scale.get, mode)

  override def size: Int = this.sizeContains

  def sizeAll: Int = if this.isEmpty then
    0
  else
    counting.toInt(this.lastIndex) - counting.toInt(this.firstIndex) + 1

  def sizeContains: Int = datMap.size

  def toDatVectorBuf: muta.DatVectorBuf[X,V] = this

  def toDatVectorFix: immu.DatVectorFix[X,V] = immu.DatVectorFix(this.iterator)

  def update(x: X, value: V): Unit = datMap += x -> value

  def +(that: IterableOnce[(X,V)]): DatVectorBuf[X,V] =
    val merge: Iterator[(X,V)] = this.iterator ++ that
    val tmpMap: mutable.TreeMap[X,V] = new mutable.TreeMap()
    for (item <- merge) do if (tmpMap.contains(item._1))
      tmpMap += item._1 -> (tmpMap(item._1) + item._2)
    else
      tmpMap += item._1 -> item._2
    DatVectorBuf[X,V](tmpMap.iterator)

  def ++(that: IterableOnce[(X,V)]): DatVectorBuf[X,V] = muta.DatVectorBuf[X,V](this.iterator ++ that)

end DatVectorBuf
