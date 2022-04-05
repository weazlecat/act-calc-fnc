package act.calc.fnc.core

case class CalcRecord(
  date: String,
  yield_rate: Double,
  street_duration: Double,
  swap_rate_at_street_duration: Double,
  spread_at_street_duration: Double,
  yield_average: Double,
  street_duration_average: Double,
  swap_rate_at_street_duration_average: Double,
  spread_at_street_duration_average: Double,
  swap_rate: Double,
  swap_rate_average: Double,
  hgb_rate: Double,
  hgb_rate_delta: Double
)