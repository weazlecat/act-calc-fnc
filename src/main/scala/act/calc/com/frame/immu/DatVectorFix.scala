package act.calc.com.frame.immu

import act.calc.com.frame.count.Counting
import act.calc.com.frame.{DatVector, immu, muta}
import act.calc.com.utils.UtilMath
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.{SortedSet, immutable, mutable}
import math.Ordering.Implicits.infixOrderingOps

class DatVectorFix[X,V](private val it: IterableOnce[(X,V)])(implicit val counting: Counting[X], val numeric: Numeric[V]) extends DatVector[X,V]:

  def this()(implicit countX: Counting[X], numeric: Numeric[V]) = this(new mutable.ListBuffer[(X,V)].iterator)(countX, numeric)

  private val datSet: immutable.TreeSet[(X,V)] =
    val builder = immutable.TreeSet.newBuilder[(X,V)]
    for elem <- it do builder += elem
    builder.result

  private val i0: Option[Int] = if datSet.isEmpty then None else Some(counting.toInt(datSet.head._1))
  private val iw: Option[Int] = if datSet.isEmpty then None else Some(counting.toInt(datSet.last._1))
  private val offset: Int = if datSet.isEmpty then 0 else i0.get

  private val datArray: Array[Option[V]] =
    if datSet.isEmpty then
      Array()
    else
      val tmpArrayBuffer: mutable.ArrayBuffer[Option[V]] = new mutable.ArrayBuffer(iw.get - i0.get + 1)
      for dat <- i0.get to iw.get do tmpArrayBuffer += None
      for dat <- datSet do tmpArrayBuffer(counting.toInt(dat._1) - offset) = Option(dat._2)
      tmpArrayBuffer.toArray

  def apply(x: X): V = datArray(counting.toInt(x) - offset).get

  def contains(x: X): Boolean = this.get(x).isDefined

  def firstIndex: X = datSet.head._1

  def firstValue: V = apply(firstIndex)

  def get(x: X): Option[V] = if isIndex(x) then datArray(counting.toInt(x) - offset) else None

  def indicesAll: SortedSet[X] =
    val builder = immutable.TreeSet.newBuilder[X]
    for x <- counting.toInt(this.firstIndex) to counting.toInt(this.lastIndex) do builder += counting.fromInt(x)
    builder.result

  def indicesContains: SortedSet[X] =
    val builder = immutable.TreeSet.newBuilder[X]
    for x <- this.iterator do if this.contains(x._1) then builder += x._1
    builder.result

  override def isEmpty: Boolean = datArray.length==0

  def isIndex(x: X): Boolean = !this.isEmpty && x >= firstIndex && x <= lastIndex

  def iterator: Iterator[(X,V)] = datSet.iterator

  def lastIndex: X = datSet.last._1

  def lastValue: V = apply(lastIndex)

  def range(from: X, until: X): DatVectorFix[X,V] = if this.isEmpty then
    DatVectorFix[X, V]()
  else
    DatVectorFix[X, V](iterator.filter(x => x._1 >= from && x._1 < until))(counting, numeric)

  def rangeFrom(from: X): DatVectorFix[X,V] = if this.isEmpty then
    DatVectorFix[X, V]()
  else
    DatVectorFix[X, V](iterator.filter(x => x._1 >= from))(counting, numeric)

  def rangeTo(to: X): DatVectorFix[X,V] = if this.isEmpty then
    DatVectorFix[X, V]()
  else
    DatVectorFix[X, V](iterator.filter(x => x._1 <= to))(counting, numeric)

  def rangeUntil(until: X): DatVectorFix[X,V] = if this.isEmpty then
    DatVectorFix[X, V]()
  else
    DatVectorFix[X, V](iterator.filter(x => x._1 < until))(counting, numeric)

  def round(scale: Int, mode: BigDecimal.RoundingMode.Value): DatVectorFix[X,V] =
    val tmpList: mutable.ListBuffer[(X,V)] = mutable.ListBuffer()
    for (item <- datSet)
      val e: X = item._1
      val v: V = item._2 match
        case d: Double => UtilMath.round(d, scale, mode).asInstanceOf[V]
        case f: Float => UtilMath.round(f, scale, mode).asInstanceOf[V]
        case _ => item._2
      tmpList += ((e, v))
    DatVectorFix[X,V](tmpList.iterator)

  def round(scale: Option[Int], mode: BigDecimal.RoundingMode.Value): DatVectorFix[X,V] = mode match
    case BigDecimal.RoundingMode.UNNECESSARY => this.toDatVectorFix
    case _ => this.round(scale.get, mode)

  override def size: Int = this.sizeContains

  def sizeAll: Int = datArray.length

  def sizeContains: Int = this.indicesContains.size

  def toDatVectorBuf: muta.DatVectorBuf[X,V] = muta.DatVectorBuf(this.iterator)

  def toDatVectorFix: immu.DatVectorFix[X,V] = immu.DatVectorFix(this.iterator)

  def +(that: IterableOnce[(X,V)]): DatVectorFix[X,V] =
    val d1: muta.DatVectorBuf[X,V] = new muta.DatVectorBuf(this.iterator)
    val d2: muta.DatVectorBuf[X,V] = new muta.DatVectorBuf(that)
    DatVectorFix[X,V]((d1 + d2).iterator)

  def ++(that: IterableOnce[(X,V)]): DatVectorFix[X,V] = immu.DatVectorFix[X,V](this.iterator ++ that)

end DatVectorFix
