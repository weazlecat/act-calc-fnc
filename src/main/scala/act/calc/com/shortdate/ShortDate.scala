package act.calc.com.shortdate

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ListBuffer

class ShortDate(yearCount: Int, monthCount: Int) extends Ordered[ShortDate]:

  def this(shortDate: ShortDate) = this(shortDate.year, shortDate.month)
  def this(count: Int) = this(ShortDate.year(count), ShortDate.month(count))
  def this(str: String) = this(str.split("-")(0).toInt, str.split("-")(1).toInt)

  val count: Int = 12 * yearCount + monthCount
  val year: Int = ShortDate.year(count)
  val month: Int = ShortDate.month(count)

  def -(that: Int): ShortDate = new ShortDate(this.year, this.month - that)
  def +(that: Int): ShortDate = new ShortDate(this.year, this.month + that)

  def -(that: ShortDate): ShortDate = new ShortDate(this.year - that.year, this.month - that.month)
  def +(that: ShortDate): ShortDate = new ShortDate(this.year + that.year, this.month + that.month)

  def before: ShortDate = this - 1
  def next: ShortDate = this + 1

  def compare(that: ShortDate): Int = this.count.compare(that.count)

  def toInt: Int = this.count

  def toLocalDate: LocalDate = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1)

  def toString(pattern: String): String = this.toLocalDate.format(DateTimeFormatter.ofPattern(pattern))

  def canEqual(that: Any): Boolean = that.isInstanceOf[ShortDate]

  override def hashCode: Int = count.hashCode

  override def equals(that: Any): Boolean = that match
    case that: ShortDate => that.canEqual(this) && this.count == that.count
    case _ => false

  override def toString: String = toString("yyyy-MM")

end ShortDate

object ShortDate:

  def range(from: ShortDate, until: ShortDate): List[ShortDate] =
    val lb: ListBuffer[ShortDate] = ListBuffer[ShortDate]()
    var s = from
    while (s<until)
      lb += s
      s = s.next
    lb.toList

  def distance(start: ShortDate, end: ShortDate): Int = end.count - start.count

  def year(countValue: Int): Int = (countValue - 1) / 12

  def month(countValue: Int): Int = (countValue - 1) % 12 + 1

end ShortDate
