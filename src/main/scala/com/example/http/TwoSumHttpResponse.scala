package com.example.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.example.Pairs
import spray.json.{DefaultJsonProtocol, JsValue, RootJsonWriter}


/**
 * Response, that is streamed by the app. Can be one of: [[TwoSumHttpPairsResponse]], [[TwoSumHttpErrorResponse]]
 */
sealed trait TwoSumHttpResponse

/**
 * Successful response, provided if the app was able to parse input sequence and search for two-sum pairs
 * @param pairs Two-sum pairs (can be empty)
 */
case class TwoSumHttpPairsResponse(pairs: Pairs) extends TwoSumHttpResponse

/**
 * Response, describing some error that has happened (usually due to parsing of CSV line)
 * Typically provided as the latest element in the stream.
 * @param errorCode Code of the error (could be used for localization)
 * @param message Detailed message of the error
 */
case class TwoSumHttpErrorResponse(errorCode: String, message: String) extends TwoSumHttpResponse

object TwoSumHttpResponse extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val pairsResponseFormat = jsonFormat1(TwoSumHttpPairsResponse.apply)
  implicit val errorResponseFormat = jsonFormat2(TwoSumHttpErrorResponse.apply)

  implicit val responseWriteFormat = new RootJsonWriter[TwoSumHttpResponse] {
    override def write(obj: TwoSumHttpResponse): JsValue = obj match {
      case pr: TwoSumHttpPairsResponse => pairsResponseFormat.write(pr)
      case er: TwoSumHttpErrorResponse => errorResponseFormat.write(er)
    }
  }

}

