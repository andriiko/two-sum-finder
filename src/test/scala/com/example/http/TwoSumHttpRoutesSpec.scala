package com.example.http

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import com.example.{TwoSumCsvController, TwoSumCsvParser}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class TwoSumHttpRoutesSpec extends AnyWordSpec with Matchers with ScalaFutures with ScalatestRouteTest {

  // the Akka HTTP route testkit does not yet support a typed actor system (https://github.com/akka/akka-http/issues/2036)
  // so we have to adapt for now
  lazy val testKit = ActorTestKit()
  implicit def typedSystem = testKit.system
  override def createActorSystem(): akka.actor.ActorSystem = testKit.system.classicSystem

  lazy val routes = new TwoSumHttpRoutes(TwoSumCsvController(TwoSumCsvParser())).twoSumRoutes

  "TwoSumHttpRoutes" should {

    "respond with pairs" in {

      val request =
        Post(uri = "/upload/0")
        .withEntity(HttpEntity.Strict(ContentTypes.`text/csv(UTF-8)`, ByteString("-1,1\n2,3,4,5\n-2,2,4,-5,5\n")))

      request ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[String] shouldEqual
           """{"pairs":[[-1,1]]}
             |{"pairs":[]}
             |{"pairs":[[-2,2],[-5,5]]}
             |""".stripMargin
      }

    }

    "respond with error if csv line is malformed" in {

      val request =
        Post(uri = "/upload/0")
          .withEntity(HttpEntity.Strict(ContentTypes.`text/csv(UTF-8)`, ByteString("-1,1\nwrong\n")))

      request ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[String] shouldEqual
          """{"pairs":[[-1,1]]}
            |{"errorCode":"400","message":"Couldn't parse one of the numbers. For input string: \"wrong\""}
            |""".stripMargin
      }

    }

  }

}
