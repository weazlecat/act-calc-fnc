package act.calc.com.frame

import scala.collection.SortedSet

trait DatVector[X,V] extends Iterable[(X,V)]:

  def apply(x: X): V

  def contains(x: X): Boolean

  def firstIndex: X

  def firstValue: V

  def get(x: X): Option[V]

  def indicesAll: SortedSet[X]

  def indicesContains: SortedSet[X]

  def isEmpty: Boolean

  def isIndex(x: X): Boolean

  def iterator: Iterator[(X,V)]
  
  def lastIndex: X

  def lastValue: V

  def range(from: X, until: X): DatVector[X,V]

  def rangeFrom(from: X): DatVector[X,V]

  def rangeTo(to: X): DatVector[X,V]

  def rangeUntil(until: X): DatVector[X,V]

  def round(scale: Int, mode: BigDecimal.RoundingMode.Value): DatVector[X,V]

  def round(scale: Option[Int], mode: BigDecimal.RoundingMode.Value): DatVector[X,V]

  def size: Int

  def toDatVectorBuf: muta.DatVectorBuf[X,V]

  def toDatVectorFix: immu.DatVectorFix[X,V]

  override def toString: String =
    var tmp = "SIZE [%d]%n".format(size)
    for (i <- iterator) do tmp += "%s -> %s%n".format(i._1.toString, i._2.toString)
    tmp
  
end DatVector
