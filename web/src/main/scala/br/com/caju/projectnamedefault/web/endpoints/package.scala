package br.com.caju.projectnamedefault.web

import br.com.caju.projectnamedefault.web.codecs.ErrorResponseCodecs
import br.com.caju.projectnamedefault.web.commons.ErrorResponse
import sttp.capabilities.zio.ZioStreams
import sttp.model.StatusCode
import sttp.tapir.server.interceptor.exception.ExceptionHandler
import sttp.tapir.server.model.ValuedEndpointOutput
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import sttp.tapir.ztapir.ZServerEndpoint
import sttp.tapir.{EndpointOutput, endpoint, statusCode}
import zio.{Task, ZIO}

package object endpoints extends ErrorResponseCodecs {

  lazy val rootEndpoint = endpoint.in("project-name-default")

  // To map de BadRequest parsing failure
  private def failureResponse(m: String) =
    ValuedEndpointOutput(
      jsonBody[ErrorResponse](
        errorResponseEncoder,
        errorResponseDecoder,
        errorResponseSchema
      ),
      ErrorResponse("PARSING_FAILURE", m)
    )

  def httpInterpreter: ZioHttpInterpreter[Any] = ZioHttpInterpreter(
    ZioHttpServerOptions.customiseInterceptors
      .defaultHandlers(failureResponse)
      .exceptionHandler(
        ExceptionHandler[Task] { _ =>
          val errorResponse = ErrorResponse.dependencyFailure
          val responseOutput: EndpointOutput[(ErrorResponse, StatusCode)] =
            jsonBody[ErrorResponse].and(statusCode)

          ZIO.some {
            ValuedEndpointOutput(
              responseOutput,
              (errorResponse, StatusCode.InternalServerError)
            )
          }
        }
      )
      .options
  )

  def toHttp[R](a: ZServerEndpoint[R, ZioStreams]) =
    httpInterpreter.toHttp(a)

}
