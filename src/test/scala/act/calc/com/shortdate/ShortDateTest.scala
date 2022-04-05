package act.calc.com.shortdate

import act.calc.com.shortdate.ShortDate
import act.calc.com.frame.count.CountingShortDate
import math.Ordering.Implicits.infixOrderingOps
import org.scalatest.funsuite.AnyFunSuite

class ShortDateTest extends AnyFunSuite {

  test("ShortDate - compare 01") {
    val v1 = new ShortDate(2000, 10)
    val v2 = new ShortDate(2000, 10)
    assert(v1 == v2)
  }

  test("ShortDate - compare 02") {
    val v1 = new ShortDate(2000, 9)
    val v2 = new ShortDate(2000, 10)
    assert(v1 < v2)
  }

  test("ShortDate - compare 03") {
    val v1 = new ShortDate(2000, 12)
    val v2 = new ShortDate(2000, 9)
    assert(v1 > v2)
  }

  test("ShortDate - minus 01") {
    val v1 = new ShortDate(2000, 3)
    val v2 = new ShortDate(1,1)
    val v3 = v1 - v2
    val v4 = new ShortDate(1999,2)
    assert(v3 == v4)
  }

  test("ShortDate - plus 01") {
    val v1 = new ShortDate(2000, 3)
    val v2 = new ShortDate(1,1)
    val v3 = v1 + v2
    val v4 = new ShortDate(2001,4)
    assert(v3 == v4)
  }

  test("ShortDate - toInt 01") {
    val v1 = new ShortDate(2000, 3)
    val v2: Int = v1.toInt
    val v3: Int = v1.count
    assert(v2 == v3)
  }

  test("ShortDate - 01") {
    val v1 = new ShortDate(2000, 10)
    val v2 = ShortDate("2000-10")
    assert(v1==v2)
  }

  test("ShortDate - 02") {
    val v1 = new ShortDate(2000, 1)
    val v2 = ShortDate("2000-10")
    assert(v1!=v2)
  }

  test("ShortDate - 03") {
    val v1 = new ShortDate(2000, 1).toString
    val v2 = "2000-01"
    assert(v1==v2)
  }

  test("ShortDate - 04") {
    val v1: ShortDate = new ShortDate(2000, 1)
    val v2: ShortDate = new ShortDate(2000, 1) + new ShortDate(0,0)
    assert(v1==v2)
  }

  test("ShortDate - 05") {
    val v1 = new ShortDate(2000, 1)
    val v2 = v1 + new ShortDate(0, 12)
    val v3 = new ShortDate(2001, 1)
    assert(v2==v3)
  }

  test("ShortDate - 06") {
    val v1 = new ShortDate(2000, 1)
    val v2 = v1 + new ShortDate(0, 13)
    val v3 = new ShortDate(2001, 2)
    assert(v2==v3)
  }

  test("ShortDate - 07") {
    val v1 = new ShortDate(2000, 1)
    val v2 = v1 - new ShortDate(0, 1)
    val v3 = new ShortDate(1999, 12)
    assert(v2==v3)
  }

  test("ShortDate - 08") {
    val v1 = new ShortDate(2000, 1)
    val v2 = v1.next
    val v3 = new ShortDate(2000, 2)
    assert(v2==v3)
  }

  test("ShortDate - 09") {
    val v1 = new ShortDate(2000, 1)
    val v2 = v1.before
    val v3 = new ShortDate(1999, 12)
    assert(v2==v3)
  }

  test("ShortDate - 10") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(1999, 13)
    assert(v1==v2)
  }

  test("ShortDate - 11") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(1999, 12)
    assert(v2<v1)
  }

  test("ShortDate - 12") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(1999, 13)
    assert(v2 <= v1)
  }

  test("ShortDate - 13") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(1999, 12)
    assert(v1>v2)
  }

  test("ShortDate - 14") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(1999, 13)
    assert(v2 >= v1)
  }

  test("ShortDate - 15") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(1999, 13)
    assert(v1 == v2)
  }

  test("ShortDate - 16") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(1999, 13)
    val l = ShortDate.range(v1, v2)
    assert(l.isEmpty)
  }

  test("ShortDate - 17") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(2000, 13)
    val l = ShortDate.range(v1, v2)
    assert(l.size == 12)
  }

  test("ShortDate - 18") {
    val v1 = new ShortDate(2000, 1)
    val v2 = new ShortDate(2000, 13)
    val l = ShortDate.range(v1, v2)
    val v3 = l.head
    assert(v1 == v3)
  }

  test("ShortDate - 19") {
    val v1 = new ShortDate(2000, 1)
    assert(v1.toString("MM.yyyy") == "01.2000")
  }

  test("ShortDate - 20") {
    val v1 = new ShortDate(2000, 1)
    assert(v1.toString("dd.MM.yyyy") == "31.01.2000")
  }

  test("ShortDate - 21") {
    val v1: ShortDate = new ShortDate(2000, 1) + new ShortDate(0,13)
    val v2: ShortDate = new ShortDate(2000, 1) + new ShortDate(1,1)
    assert(v1==v2)
  }

  test("ShortDate - 22") {
    val v1: ShortDate = new ShortDate(2000, 1) - new ShortDate(0,13)
    val v2: ShortDate = new ShortDate(2000, 1) - new ShortDate(1,1)
    assert(v1==v2)
  }

}
