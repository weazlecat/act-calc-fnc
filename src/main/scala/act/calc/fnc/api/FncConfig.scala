package act.calc.fnc.api

import act.calc.com.{FileId, FileIdPath}
import act.calc.fnc.api.FncConfig.{CalcConf, CalcOptimizationConf, CalcRoundingConf, ProjectionConf, ProjectionCsvConf, ProjectionParameterConf, ReportConf, ReportParameterConf, ReportTemplateConf, StockConf, StockCsvConf}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class FncConfig(filenameOfConfiguration: String):

  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  private val FncStockCsvFilename = "fnc.stock.csv.filename"
  private val FncStockCsvFilepath = "fnc.stock.csv.filepath"
  private val FncStockCsvFilelocation = "fnc.stock.csv.filelocation"
  private val FncStockCsvDelimiter = "fnc.stock.csv.delimiter"
  private val FncOptimizationRelativeTolerance = "fnc.optimization.relative_tolerance"
  private val FncOptimizationAboluteTolerance = "fnc.optimization.absolute_tolerance"
  private val FncOptimizationMaxEval = "fnc.optimization.max_eval"
  private val FncOptimizationSearchIntervalMin = "fnc.optimization.search_interval_min"
  private val FncOptimizationSearchIntervalMax = "fnc.optimization.search_interval_max"
  private val FncRoundingYieldRate = "fnc.rounding.yield_rate"
  private val FncRoundingStreetDuration = "fnc.rounding.street_duration"
  private val FncRoundingSwapRateAtStreetDuration = "fnc.rounding.swap_rate_at_street_duration"
  private val FncRoundingSpreadAtStreetDuration = "fnc.rounding.spread_at_street_duration"
  private val FncRoundingYieldRateAverage = "fnc.rounding.yield_rate_average"
  private val FncRoundingStreetDurationAverage = "fnc.rounding.street_duration_average"
  private val FncRoundingSwapRateAtStreetDurationAverage = "fnc.rounding.swap_rate_at_street_duration_average"
  private val FncRoundingSpreadAtStreetDurationAverage = "fnc.rounding.spread_at_street_duration_average"
  private val FncRoundingSwapRate = "fnc.rounding.swap_rate"
  private val FncRoundingSwapRateAverage = "fnc.rounding.swap_rate_average"
  private val FncRoundingHgbRate = "fnc.rounding.hgb_rate"
  private val FncRoundingHgbRateDelta = "fnc.rounding.hgb_rate_delta"
  private val FncReportPdfFilepath = "fnc.report.pdf.filepath"
  private val FncReportPdfFilelocation = "fnc.report.pdf.filelocation"
  private val FncReportParameterDistanceInMonthStd = "fnc.report.parameter.distance_in_month_std"
  private val FncReportTemplateSourceFilepath = "fnc.report.template.source.filepath"
  private val FncReportTemplateSourceFilelocation = "fnc.report.template.source.filelocation"
  private val FncReportTemplateTargetFilepath = "fnc.report.template.target.filepath"
  private val FncReportTemplateTargetFilelocation = "fnc.report.template.target.filelocation"
  private val FncReportTemplatePdfImgSwap = "fnc.report.template.pdf_img_swap"
  private val FncReportTemplatePdfImgSpread = "fnc.report.template.pdf_img_spread"
  private val FncReportTemplatePdfPic = "fnc.report.template.pdf_pic"
  private val FncReportTemplatePdfCss = "fnc.report.template.pdf_css"
  private val FncProjectionCsvFilepath = "fnc.projection.csv.filepath"
  private val FncProjectionCsvFilelocation = "fnc.projection.csv.filelocation"
  private val FncProjectionCsvDelimiter = "fnc.projection.csv.delimiter"
  private val FncProjectionParameterDistanceInMonthStd = "fnc.projection.parameter.distance_in_month_std"

  logger.info("Getting Finance Configuration - BEGIN FILE[%s]".format(filenameOfConfiguration))
  private val conf = ConfigFactory.load(filenameOfConfiguration)

  // ----- Configuration STOCK
  private val stockCsvFileId = FileId(
    conf.getString(FncStockCsvFilename),
    FileIdPath(
      conf.getString(FncStockCsvFilepath),
      FileIdPath.Location.valueOf(conf.getString(FncStockCsvFilelocation).toUpperCase)
    )
  )
  private val stockCsvDelimiter = conf.getString(FncStockCsvDelimiter)
  private val stockCsv = StockCsvConf(stockCsvFileId, stockCsvDelimiter)

  // ----- Configuration CALCULATOR
  private val calcOptimization = CalcOptimizationConf(
    conf.getString(FncOptimizationRelativeTolerance).toDouble,
    conf.getString(FncOptimizationAboluteTolerance).toDouble,
    conf.getString(FncOptimizationMaxEval).toInt,
    conf.getString(FncOptimizationSearchIntervalMin).toDouble,
    conf.getString(FncOptimizationSearchIntervalMax).toDouble
  )
  private val calcRounding = CalcRoundingConf(
    conf.getString(FncRoundingYieldRate).toInt,
    conf.getString(FncRoundingStreetDuration).toInt,
    conf.getString(FncRoundingSwapRateAtStreetDuration).toInt,
    conf.getString(FncRoundingSpreadAtStreetDuration).toInt,
    conf.getString(FncRoundingYieldRateAverage).toInt,
    conf.getString(FncRoundingStreetDurationAverage).toInt,
    conf.getString(FncRoundingSwapRateAtStreetDurationAverage).toInt,
    conf.getString(FncRoundingSpreadAtStreetDurationAverage).toInt,
    conf.getString(FncRoundingSwapRate).toInt,
    conf.getString(FncRoundingSwapRateAverage).toInt,
    conf.getString(FncRoundingHgbRate).toInt,
    conf.getString(FncRoundingHgbRateDelta).toInt
  )

  // ----- Configuration REPORT
  private val reportPdfFileIdPath = FileIdPath(
      conf.getString(FncReportPdfFilepath),
      FileIdPath.Location.valueOf(conf.getString(FncReportPdfFilelocation).toUpperCase)
  )
  private val reportParameter: ReportParameterConf = ReportParameterConf(
    conf.getString(FncReportParameterDistanceInMonthStd).toInt
  )
  private val reportTemplate = ReportTemplateConf(
    new FileIdPath(conf.getString(FncReportTemplateSourceFilepath), FileIdPath.Location.valueOf(conf.getString(FncReportTemplateSourceFilelocation).toUpperCase)),
    new FileIdPath(conf.getString(FncReportTemplateTargetFilepath), FileIdPath.Location.valueOf(conf.getString(FncReportTemplateTargetFilelocation).toUpperCase)),
    conf.getString(FncReportTemplatePdfImgSwap),
    conf.getString(FncReportTemplatePdfImgSpread),
    conf.getString(FncReportTemplatePdfPic),
    conf.getString(FncReportTemplatePdfCss)
  )

  // ----- Configuration PROJECTION
  private val projectionCsvFileIdPath = FileIdPath(
      conf.getString(FncProjectionCsvFilepath),
      FileIdPath.Location.valueOf(conf.getString(FncProjectionCsvFilelocation).toUpperCase)
  )
  private val projectionCsvDelimiter = conf.getString(FncProjectionCsvDelimiter)
  private val projectionCsv = ProjectionCsvConf(projectionCsvFileIdPath, projectionCsvDelimiter)

  private val projectionParameter = ProjectionParameterConf(
    conf.getString(FncProjectionParameterDistanceInMonthStd).toInt
  )

  // -----
  val stock: StockConf = StockConf(stockCsv)
  val calc: CalcConf = CalcConf(calcOptimization, calcRounding)
  val repo: ReportConf = ReportConf(reportPdfFileIdPath, reportParameter, reportTemplate)
  val proj: ProjectionConf = ProjectionConf(projectionCsv, projectionParameter)

  logger.info("Getting Finance Configuration - END")
  
end FncConfig

object FncConfig:

  // ----- Configuration STOCK
  case class StockConf(csv: StockCsvConf)
  case class StockCsvConf(fileId: FileId, delimiter: String)

  // ----- Configuration CALCULATOR
  case class CalcConf(optimization: CalcOptimizationConf, rounding: CalcRoundingConf)
  case class CalcOptimizationConf(
    relativeTolerance: Double,
    absoluteTolerance: Double,
    maxEval: Int,
    searchIntervalMin: Double,
    searchIntervalMax: Double
  )
  case class CalcRoundingConf(
    yieldRate: Int,
    streetDuration: Int,
    swapRateAtStreetDuration: Int,
    spreadAtStreetDuration: Int,
    yieldRateAverage: Int,
    streetDurationAverage: Int,
    swapRateAtStreetDurationAverage: Int,
    spreadAtStreetDurationAverage: Int,
    swapRate: Int,
    swapRateAverage: Int,
    hgbRate: Int,
    hgbRateDelta: Int
  )

  // ----- Configuration REPORT
  case class ReportConf(pdf: FileIdPath, parameter: ReportParameterConf, template: ReportTemplateConf)
  case class ReportParameterConf (
    distanceInMonthStd: Int
  )
  case class ReportTemplateConf(
    source: FileIdPath,
    target: FileIdPath,
    pdfImgSwap: String,
    pdfImgSpread: String,
    pdfPic: String,
    pdfCss: String
  )

  // ----- Configuration PROJECTION
  case class ProjectionConf(csv: ProjectionCsvConf, parameter: ProjectionParameterConf)
  case class ProjectionCsvConf(fileIdPath: FileIdPath, delimiter: String)
  case class ProjectionParameterConf(distanceInMonthStd: Int)

end FncConfig
