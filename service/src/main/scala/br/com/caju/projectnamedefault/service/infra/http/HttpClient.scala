package br.com.caju.projectnamedefault.service.infra.http

import br.com.caju.projectnamedefault.lib.logger.LoggerUtils
import br.com.caju.projectnamedefault.lib.logger.LoggerUtils.ZIOLogOps
import sttp.capabilities.zio.ZioStreams
import sttp.client3.{DeserializationException, HttpError, Request, Response, ResponseException, SttpApi}
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.model.StatusCode
import zio.{IO, ZIO, ZLayer}

class HttpClient(private val sttpClient: SttpClient) extends SttpApi {

  private val CorrelationIdHeader = "X-Caju-Correlation-Id"

  /** Sends a request over the internet with Caju correlation Id header.
    * Use this instead of `send` when calling Caju internal services
    *
    * @param request request description
    * @tparam A expected response type
    * @return [[Response]] of the expected type
    */
  def sendCorrelated[A](
      request: Request[A, ZioStreams]
  ): IO[HttpClientError, Response[A]] =
    for {
      correlationId <- LoggerUtils.getCorrelationId
      res <- sttpClient
               .send(request.header(CorrelationIdHeader, correlationId))
               .tapError(e =>
                 ZIO
                   .logError("Error sending request")
                   .withLogAnnotation("requestCurl", request.toCurl)
                   .withLogAnnotation("error", e.getMessage)
               )
               .orElseFail(RequestSendingError)
    } yield res

  /** Handles the response by verifying the response code without deserialization
    * @param response request response
    * @param requestCurl original request for logging purposes
    * @tparam R handled pure response value type
    */
  def handleResponse[R](
      response: Response[Either[ResponseException[String, String], R]],
      requestCurl: => String
  ): ZIO[Any, HttpClientError, R] =
    ZIO
      .fromEither(response.body)
      .tapError(body =>
        ZIO
          .logError("Got error when making request")
          .withLogAnnotation("statusText", response.statusText)
          .withLogAnnotation("body", body.toString)
          .withLogAnnotation("requestCurl", requestCurl)
      )
      .mapError {
        case HttpError(body, _)             => FailResponseError(response.code, body)
        case DeserializationException(_, _) => ResponseDeserializationError
      }

  /** Handles the response by verifying the response code without response deserialization
    *
    * @param response    request response
    * @param requestCurl original request for logging purposes
    * @tparam R handled pure response value type
    */
  def handleResponse[R](
      response: Response[Either[String, R]],
      requestCurl: String
  ): ZIO[Any, HttpClientError, R] =
    ZIO
      .fromEither(response.body)
      .tapError(body =>
        ZIO
          .logError("Got error when making request")
          .withLogAnnotation("statusText", response.statusText)
          .withLogAnnotation("body", body)
          .withLogAnnotation("requestCurl", requestCurl)
      )
      .mapError(body => FailResponseError(response.code, body))
}

sealed trait HttpClientError
case object RequestSendingError                                            extends HttpClientError
case object ResponseDeserializationError                                   extends HttpClientError
case class FailResponseError(statusCode: StatusCode, responseBody: String) extends HttpClientError

object HttpClient {
  val layer: ZLayer[SttpClient, Nothing, HttpClient] =
    ZLayer.fromFunction(new HttpClient(_))
}
