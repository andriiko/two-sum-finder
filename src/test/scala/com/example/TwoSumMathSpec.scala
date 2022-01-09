package com.example

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class TwoSumMathSpec extends AnyWordSpec with Matchers {

  "TwoSumMath" should {

    "find pairs for basic case" in {
      TwoSumMath
        .findPairs(0, Seq(9, 5, 3, -8, 1, -2, 8, -5, -3))
        .map { _.sorted } should contain theSameElementsAs Seq(Seq(-3, 3), Seq(-8, 8), Seq(-5, 5))

    }

    "find multiple same pairs" in {
      TwoSumMath
        .findPairs(4, Seq(1, 3, 3, 1))
        .map { _.sorted } should contain theSameElementsAs Seq(Seq(1, 3), Seq(1, 3))

    }

    "find single pair if another pair element is exhausted" in {
      TwoSumMath
        .findPairs(4, Seq(1, 3, 3))
        .map { _.sorted } should contain theSameElementsAs Seq(Seq(1, 3))

    }

    "find no pairs if no matches can be found" in {
      TwoSumMath
        .findPairs(-100, Seq(1, 2, 3, 4, 5))
        .map { _.sorted } shouldEqual Nil

    }

    "find no pairs for empty list" in {
      TwoSumMath
        .findPairs(-100, Nil)
        .map { _.sorted } shouldEqual Nil

    }

  }

}
