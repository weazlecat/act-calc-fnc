package act.calc.com.frame

import scala.collection.SortedSet

trait DatFrame[X,Y,V] extends Iterable[(X,Y,V)]:

  def apply(x: X, y: Y): V

  def column(y: Y): DatVector[X,V]

  def columnsAll: SortedSet[Y]

  def columnsContains: SortedSet[Y]

  def firstColumn: Y

  def firstRow: X

  def get(x: X, y: Y): Option[V]

  def isColumn(y: Y): Boolean

  def isEmpty: Boolean

  def isRow(x: X): Boolean

  def iterator: Iterator[(X,Y,V)]

  def lastColumn: Y

  def lastRow: X

  def row(x: X): DatVector[Y,V]

  def rowsAll: SortedSet[X]

  def rowsContains: SortedSet[X]

  def toDatFrameBuf: muta.DatFrameBuf[X,Y,V]

  def toDatFrameFix: immu.DatFrameFix[X,Y,V]

end DatFrame
