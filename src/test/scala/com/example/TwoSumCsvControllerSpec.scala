package com.example

import scala.util.{Failure, Success}
import org.mockito.scalatest.IdiomaticMockito
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class TwoSumCsvControllerSpec extends AnyWordSpec with Matchers with IdiomaticMockito {

  "TwoSumCsvController" should {

    "succeed for basic case" in {
      val parser = mock[TwoSumCsvParser]
      parser.extractNumbers("1,2,3") shouldReturn Success(Seq(1, 2, 3))

      TwoSumCsvController(parser).findPairsInCsv(3, "1,2,3") shouldEqual Success(Seq(Seq(1, 2)))

      parser.extractNumbers("1,2,3") was called
    }

    "propagate parsing error for malformed csv" in {
      val parser = mock[TwoSumCsvParser]
      parser.extractNumbers("1,ttrt") shouldReturn Failure(new NumberFormatException("Cannot parse"))

      TwoSumCsvController(parser).findPairsInCsv(3, "1,ttrt") shouldBe a[Failure[NumberFormatException]]

      parser.extractNumbers("1,ttrt") was called
    }

  }

}
