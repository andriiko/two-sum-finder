package com.example

import scala.util.Try

/**
 * Parses input csv line
 */
trait TwoSumCsvParser {

  /**
   * Extracts comma-separated numbers
   * @param csv CSV line
   * @return Numbers or error if parsing failed
   */
  def extractNumbers(csv: String): Try[Seq[Int]]

}

object TwoSumCsvParser {

  def apply(): TwoSumCsvParser = new TwoSumCsvParser {

    override def extractNumbers(csv: String): Try[Seq[Int]] = Try {
      csv.split(",").toSeq.map { _.trim.toInt } // trivial parsing/error handling for now
    }

  }

}
