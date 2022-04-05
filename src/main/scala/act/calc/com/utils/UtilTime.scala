package act.calc.com.utils

import act.calc.com.Attribute.ActuarialAgeMethod
import java.time.LocalDate

object UtilTime:

  /**
   * BGB age: §187 Abs. 2 Satz 2 BGB und §188 Abs. 2 Alt. 2 BGB
   *
   * @param dateActual  actual date
   * @param dateOfBirth birthday
   * @return real age according to the birthday.
   */
  def ageBGB(dateActual: LocalDate, dateOfBirth: LocalDate): Int =
    dateActual.getYear - dateOfBirth.getYear
      + {
      if dateActual.getMonthValue < dateOfBirth.getMonthValue then -1 else 0
    }
      + {
      if dateActual.getMonthValue == dateOfBirth.getMonthValue && dateActual.getDayOfMonth < dateOfBirth.getDayOfMonth then -1 else 0
    }

  /**
   * Calendrical age
   *
   * @param dateActual  actual date
   * @param dateOfBirth birthday
   * @return calendrical age as actual year minus year of birth
   */
  def ageCalendrical(dateActual: LocalDate, dateOfBirth: LocalDate): Int = dateActual.getYear - dateOfBirth.getYear

  /**
   * Actuarial age
   *
   * f.e date 31.12.YYYY has birthday from 1.7.YYYY to 30.06.(YYYY+1) as the actuarial age
   *
   * @param dateActual  actual date
   * @param dateOfBirth birthday
   * @return actuarial age as the nearest age to the given date
   */
  def ageActuarial(dateActual: LocalDate, dateOfBirth: LocalDate): Int = ageBGB(dateActual.minusMonths(6), dateOfBirth) + 1

  /**
   * Determine the age with method calcAgeId
   *
   * @param dateActual actual date
   * @param dateOfBirth date of birth
   * @param ageMethod method
   * @return actualAge
   */
  def ageByMethod(dateActual: LocalDate, dateOfBirth: LocalDate, ageMethod: ActuarialAgeMethod): Int =
    ageMethod match
      case ActuarialAgeMethod.CALENDRIC => ageCalendrical(dateActual, dateOfBirth)
      case ActuarialAgeMethod.STATUTORY => ageBGB(dateActual, dateOfBirth)
      case ActuarialAgeMethod.ACTUARIAL => ageActuarial(dateActual, dateOfBirth)

end UtilTime
