package com.example

import scala.util.Try


/**
 * Performs input validation and might contain some additional app logic (security, metrics etc.)
 */
trait TwoSumCsvController {

  /**
   * Tries to find pairs in input csv line
   * @param targetSum Target sum
   * @param csvNumbers Csv line with numbers
   * @return Pairs or error if csv couldn't be extracted
   */
  def findPairsInCsv(targetSum: Int, csvNumbers: String): Try[Pairs]

}

object TwoSumCsvController {

  def apply(csvParsing: TwoSumCsvParser): TwoSumCsvController = new TwoSumCsvController {

    override def findPairsInCsv(targetSum: Int, csvNumbers: String): Try[Pairs] = {
      csvParsing
        .extractNumbers(csvNumbers)
        .map { TwoSumMath.findPairs(targetSum, _) }

    }
  }

}



