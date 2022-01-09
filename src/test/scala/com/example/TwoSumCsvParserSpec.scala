package com.example

import scala.util.{Failure, Success}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class TwoSumCsvParserSpec extends AnyWordSpec with Matchers {

  "TwoSumCsvParser" should {

    "extract numbers from basic csv" in {
      TwoSumCsvParser().extractNumbers("-1,3,5") shouldEqual Success(Seq(-1,3,5))
    }

    "trim spaces when extracting" in {
      TwoSumCsvParser().extractNumbers("-1 , 3, 5") shouldEqual Success(Seq(-1,3,5))
    }

    "fail parsing csv with letters" in {
      TwoSumCsvParser().extractNumbers("-1,baba") shouldBe a[Failure[_]]
    }

  }

}
