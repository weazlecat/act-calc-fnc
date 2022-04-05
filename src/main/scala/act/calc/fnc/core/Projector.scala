package act.calc.fnc.core

import act.calc.com.shortdate.ShortDate
import act.calc.com.FileId
import act.calc.com.utils.UtilFile
import act.calc.fnc.api.FncConfig
import act.calc.fnc.data.ProjectionRecord

import scala.collection.mutable

class Projector(calculator: Calculator, confProj: FncConfig.ProjectionConf):

  confProj.csv.fileIdPath.createDirectory

  def project(dateOfReportFrom: ShortDate, dateOfReportTo: ShortDate = null, average: Int, filename: String) : Unit =
    val dateOfReportToCalc = if (dateOfReportTo == null) dateOfReportFrom + confProj.parameter.distanceInMonthStd else dateOfReportTo
    val itemTable = new mutable.HashMap[ShortDate, ProjectionRecord]()
    val distanceInMonth = ShortDate.distance(dateOfReportFrom, dateOfReportToCalc)
    for (month <- 0 to distanceInMonth) itemTable += (dateOfReportFrom + month -> ProjectionRecord(DATE=(dateOfReportFrom + month).toString("dd.MM.yyyy")))
    for (maturity <- 1 to 50) 
      val p = calculator.projection(dateOfReportToCalc, maturity, average)
      for (month <- 0 to distanceInMonth) 
        val date = dateOfReportFrom + month
        val record: ProjectionRecord = itemTable(date)
        record.swap(maturity, p.select(date).hgb_rate)

    val items: mutable.ListBuffer[ProjectionRecord] = new mutable.ListBuffer[ProjectionRecord]()
    for (month <- 0 to distanceInMonth) items += itemTable(dateOfReportFrom + month)

    val fileId = FileId(filename, confProj.csv.fileIdPath)
    UtilFile.csvWrite[ProjectionRecord](items.toList, fileId, confProj.csv.delimiter(0))
  
end Projector
