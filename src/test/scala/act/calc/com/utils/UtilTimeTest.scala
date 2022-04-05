package act.calc.com.utils

import org.scalatest.funsuite.AnyFunSuite
import act.calc.com.Attribute.ActuarialAgeMethod

import java.time.{LocalDate, Month}

class UtilTimeTest extends AnyFunSuite:

  test("DateTime 01") {
    val d1 = LocalDate.of(2000, 6, 1)
    val d2 = LocalDate.of(2000, Month.JUNE, 1)
    assert(d1 == d2 && d1.isEqual(d2) && d1.equals(d2))
  }

  test("UtilTime ageCalendrical 01") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 6, 1)
    val dateActual  = LocalDate.of(yearOfActual, 7, 1)
    assert(UtilTime.ageCalendrical(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageBGB 01") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 6, 15)
    val dateActual  = LocalDate.of(yearOfActual, 7, 15)
    assert(UtilTime.ageBGB(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageBGB 02") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 6, 15)
    val dateActual  = LocalDate.of(yearOfActual, 6, 15)
    assert(UtilTime.ageBGB(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageBGB 03") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 6, 15)
    val dateActual  = LocalDate.of(yearOfActual, 6, 14)
    assert(UtilTime.ageBGB(dateActual, dateOfBirth) == yearOfActual-yearOfBirth-1)
  }

  test("UtilTime ageBGB 04") {
    val yearOfBirth = 2000
    val yearOfActual = 2004
    val dateOfBirth = LocalDate.of(yearOfBirth, 2, 28)
    val dateActual  = LocalDate.of(yearOfActual, 2, 28)
    assert(UtilTime.ageBGB(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageBGB 05") {
    val yearOfBirth = 2000
    val yearOfActual = 2004
    val dateOfBirth = LocalDate.of(yearOfBirth, 2, 28)
    val dateActual  = LocalDate.of(yearOfActual, 2, 29)
    assert(UtilTime.ageBGB(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageBGB 06") {
    val yearOfBirth = 2000
    val yearOfActual = 2004
    val dateOfBirth = LocalDate.of(yearOfBirth, 3, 1)
    val dateActual  = LocalDate.of(yearOfActual, 2, 29)
    assert(UtilTime.ageBGB(dateActual, dateOfBirth) == yearOfActual-yearOfBirth-1)
  }

  test("UtilTime ageActuarial 01") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 10, 2)
    val dateActual  = LocalDate.of(yearOfActual, 11, 2)
    assert(UtilTime.ageActuarial(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageActuarial 02") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 10, 2)
    val dateActual  = LocalDate.of(yearOfActual, 11, 1)
    assert(UtilTime.ageActuarial(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageActuarial 03") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 10, 2)
    val dateActual  = LocalDate.of(yearOfActual, 9, 2)
    assert(UtilTime.ageActuarial(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageActuarial 04") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 10, 2)
    val dateActual  = LocalDate.of(yearOfActual, 4, 3)
    assert(UtilTime.ageActuarial(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageActuarial 05") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 10, 2)
    val dateActual  = LocalDate.of(yearOfActual, 4, 2)
    assert(UtilTime.ageActuarial(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageActuarial 06") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 10, 2)
    val dateActual  = LocalDate.of(yearOfActual, 4, 1)
    assert(UtilTime.ageActuarial(dateActual, dateOfBirth) == yearOfActual-yearOfBirth-1)
  }

  test("UtilTime ageActuarial 07") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 4, 2)
    val dateActual  = LocalDate.of(yearOfActual, 4, 1)
    assert(UtilTime.ageActuarial(dateActual, dateOfBirth) == yearOfActual-yearOfBirth)
  }

  test("UtilTime ageByMethod 01") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 4, 2)
    val dateActual  = LocalDate.of(yearOfActual, 4, 1)
    assert(
      UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.ACTUARIAL) == UtilTime.ageActuarial(dateActual, dateOfBirth) &&
        UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.STATUTORY) == UtilTime.ageBGB(dateActual, dateOfBirth) &&
        UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.CALENDRIC) == UtilTime.ageCalendrical(dateActual, dateOfBirth)
    )
  }

  test("UtilTime ageByMethod 02") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 4, 1)
    val dateActual  = LocalDate.of(yearOfActual, 4, 2)
    assert(
      UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.ACTUARIAL) == UtilTime.ageActuarial(dateActual, dateOfBirth) &&
        UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.STATUTORY) == UtilTime.ageBGB(dateActual, dateOfBirth) &&
        UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.CALENDRIC) == UtilTime.ageCalendrical(dateActual, dateOfBirth)
    )
  }

  test("UtilTime ageByMethod 03") {
    val yearOfBirth = 1970
    val yearOfActual = 2015
    val dateOfBirth = LocalDate.of(yearOfBirth, 3, 1)
    val dateActual  = LocalDate.of(yearOfActual, 2, 28)
    assert(
      UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.ACTUARIAL) == UtilTime.ageActuarial(dateActual, dateOfBirth) &&
        UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.STATUTORY) == UtilTime.ageBGB(dateActual, dateOfBirth) &&
        UtilTime.ageByMethod(dateActual, dateOfBirth, ActuarialAgeMethod.CALENDRIC) == UtilTime.ageCalendrical(dateActual, dateOfBirth)
    )
  }

end UtilTimeTest
