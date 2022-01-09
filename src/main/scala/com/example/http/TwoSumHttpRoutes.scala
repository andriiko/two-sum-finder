package com.example.http

import scala.concurrent.duration.DurationInt
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Flow, Framing, Source}
import akka.util.ByteString
import com.example.TwoSumCsvController
import com.example.http.TwoSumHttpResponse._

class TwoSumHttpRoutes(controller: TwoSumCsvController)(implicit val system: ActorSystem[_]) {

  private val newLine = ByteString("\n")
  private implicit val jsonStreaming = EntityStreamingSupport.json().withFramingRenderer(Flow[ByteString].map {_ ++ newLine})

  val twoSumRoutes: Route = {
    path("upload" / IntNumber) { targetSum =>
      post {
        extractRequestEntity { entity =>
          val src: Source[TwoSumHttpResponse, Any] =
            entity
            .dataBytes
            .via(Framing.delimiter(newLine, 1024))
            .map { _.utf8String }
            .map { controller.findPairsInCsv(targetSum, _).get }
//            .throttle(1, 1.second)  // to feel sense of streaming
            .map { TwoSumHttpPairsResponse }
            .recover {
              case ex: NumberFormatException => TwoSumHttpErrorResponse("400", s"Couldn't parse one of the numbers. ${ex.getMessage}")
              case ex: Throwable => TwoSumHttpErrorResponse("500", s"Internal server error: ${ex.getMessage}")
            }

          complete(src)
        }
      }
    }
  }

}
