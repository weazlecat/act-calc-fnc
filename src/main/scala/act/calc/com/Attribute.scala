package act.calc.com

import act.calc.com.frame.count.Counting

/**
 * Global identifiers to be used in the project
 *
 */
object Attribute:

  /**
   *  Gender of a person
   */
  enum Gender extends Ordered[Gender]:
    case MALE
    case FEMALE
    case DIVERS
    def compare(that: Gender): Int = this.ordinal.compare(that.ordinal)
  end Gender

  enum PopuModel:
    case q_aa
    case q_a
    case i
    case q_i
    case q_g
    case q_r
    case y
    case h
    case q_w
    case fk
    case fl
    case fk_aa
    case fl_aa
    case fk_i
    case fl_i
    case fk_g
    case fl_g
    case fk_r
    case fl_r
    case fk_w
    case fl_w
  end PopuModel

  enum MortModelRes:
    case q_0
    case q_1
    case q_2
    case q_3
    case q_4
    case q_5
    case q_6
    case fk
    case fl
    case av
  end MortModelRes

  enum MortModel:
    case q
  end MortModel

  enum DisaModelRes:
    case i
    case q_a
    case r_0
    case r_1
    case r_2
    case r_3
    case r_4
    case r_5
    case r_6
    case r_7
    case r_8
    case r_9
    case q_i_0
    case q_i_1
    case q_i_2
    case q_i_3
    case q_i_4
    case q_i_5
  end DisaModelRes

  enum DisaModel:
    case i
    case q_a
    case r
    case q_i
  end DisaModel

  enum DataSetStyle extends Ordered[DataSetStyle]:
    case DISABILITY
    case FLUCTUATION
    case MORTALITY
    case POPULATION_MODEL
    def compare(that: DataSetStyle): Int = this.ordinal.compare(that.ordinal)
  end DataSetStyle

  enum TableBuildMethodPopu extends Ordered[TableBuildMethodPopu]:
    case PERIOD
    case GENERATION_BY_TREND
    def compare(that: TableBuildMethodPopu): Int = this.ordinal.compare(that.ordinal)
  end TableBuildMethodPopu

  enum TableBuildMethodMort extends Ordered[TableBuildMethodMort]:
    case PERIOD
    case GENERATION_BY_SHIFT_OF_AGE
    case GENERATION_BY_TREND
    def compare(that: TableBuildMethodMort): Int = this.ordinal.compare(that.ordinal)
  end TableBuildMethodMort

  enum GenderStrategy extends Ordered[GenderStrategy]:
    case GENDERSPECIFIC
    case UNISEX
    def compare(that: GenderStrategy): Int = this.ordinal.compare(that.ordinal)
  end GenderStrategy

  enum RoundingMode extends Ordered[RoundingMode]:
    case HALF_EVEN
    case HALF_UP
    case NONE
    def compare(that: RoundingMode): Int = this.ordinal.compare(that.ordinal)
    def toBigDecimalRoundingMode: BigDecimal.RoundingMode.Value = this match
      case HALF_EVEN => BigDecimal.RoundingMode.HALF_EVEN
      case HALF_UP => BigDecimal.RoundingMode.HALF_UP
      case NONE => BigDecimal.RoundingMode.UNNECESSARY
  end RoundingMode

  enum TrendInvalidMethod extends Ordered[TrendInvalidMethod]:
    case METHOD_2005
    case METHOD_2018
    def compare(that: TrendInvalidMethod): Int = this.ordinal.compare(that.ordinal)
  end TrendInvalidMethod

  enum TotalStockMethod extends Ordered[TotalStockMethod]:
    case FROM_TABLE_A_CALCULATION
    case FROM_TABLE_G_LISTING
    def compare(that: TotalStockMethod): Int = this.ordinal.compare(that.ordinal)
  end TotalStockMethod

  enum PensionerMortalityMethod extends Ordered[PensionerMortalityMethod]:
    case START_FROM_FINAL_AGE
    case START_FROM_END_OF_A
    def compare(that: PensionerMortalityMethod): Int = this.ordinal.compare(that.ordinal)
  end PensionerMortalityMethod

  enum UnisexBuildMethod extends Ordered[UnisexBuildMethod]:
    case BASIS
    case PERSONSPECIFIC
    def compare(that: UnisexBuildMethod): Int = this.ordinal.compare(that.ordinal)
  end UnisexBuildMethod

  enum UnisexAgeDifferenceMethod extends Ordered[UnisexAgeDifferenceMethod]:
    case MIN
    case MIX
    def compare(that: UnisexAgeDifferenceMethod): Int = this.ordinal.compare(that.ordinal)
  end UnisexAgeDifferenceMethod

  enum ActuarialPersonType:
    case INDIVIDUAL
    case COLLECTIVE
  end ActuarialPersonType

  /**
   *  Attributes to determine surviving spouse
   */
  enum ActuarialSpouseMethod:
    case COLL
    case IND_AGE
    case IND_YOB
    case NONE
  end ActuarialSpouseMethod

  /**
   *  Method how to calculate a persons age
   */
  enum ActuarialAgeMethod:
    case ACTUARIAL
    case CALENDRIC
    case STATUTORY
  end ActuarialAgeMethod

  enum LfeCalculus:
    case TW2004
  end LfeCalculus
  
end Attribute

