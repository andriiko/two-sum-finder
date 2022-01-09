package com.example

import scala.collection.mutable.{Map => MMap}


object TwoSumMath {

  /**
   * Finds pairs, sum of which equals to [[target]].
   *
   * Uses map to keep how many elements are available for building matching pairs.
   * Then traverses the input list to find an appropriate match.
   *
   * The complexity is O(N).
   *
   * An alternative approach could be sorting an input array and then using left/right cursors to find pairs.
   * But complexity of this approach would be O(NlogN).
   *
   * @param targetSum Target sum
   * @param numbers Source numbers
   * @return List of pairs
   */
  def findPairs(targetSum: Int, numbers: Seq[Int]): Pairs = {
    val availabilityCounts = MMap.empty[Int, Int]
    numbers.foreach { availabilityCounts.updateWith(_) { maybeCount => Some(maybeCount.getOrElse(0) + 1) } }   // could be used groupBy->mapValues instead, but less memory-efficient

    numbers.map { n =>
      val nOther = targetSum - n
      availabilityCounts.get(nOther) match {
        case None => // not found
          Nil
        case Some(0) => // exhausted
          Nil
        case Some(1) if n == nOther => // exhausted
          Nil
        case Some(otherCount) if n == nOther =>  // e.g. n:3 + nOther:3 = targetSum:6
          availabilityCounts.update(nOther, otherCount - 2)
          Seq(n, nOther)
        case Some(_) if availabilityCounts(n) == 0 => // exhausted
          Nil
        case Some(otherCount) =>
          availabilityCounts.update(n, availabilityCounts(n) - 1)
          availabilityCounts.update(nOther, otherCount - 1)
          Seq(n, nOther)
      }
    }.filter { _.nonEmpty }

  }

}
