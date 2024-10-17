package br.com.caju.projectnamedefault.web.endpoints

import br.com.caju.projectnamedefault.web.commons.ErrorResponse
import sttp.model.StatusCode
import sttp.tapir.EndpointIO.Example
import sttp.tapir.docs.openapi.OpenAPIDocsOptions
import sttp.tapir.swagger.SwaggerUIOptions
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.{EndpointInput, statusCode}
import zio.Task

object DocsEndpoint {

  // Default badRequest error for parsing failure
  private val decodeFailureOutput = (_: EndpointInput[_]) =>
    Some(
      statusCode(StatusCode.BadRequest).and(
        jsonBody[ErrorResponse]
          .examples(
            List(
              Example(
                ErrorResponse("PARSING_FAILURE", "error description"),
                Some("PARSING_FAILURE"),
                None
              )
            )
          )
          .description("Some parsing failure")
      )
    )

  private val openApiDocsConfig = OpenAPIDocsOptions.default
    .copy(defaultDecodeFailureOutput = decodeFailureOutput)

  def endpoints =
    httpInterpreter.toHttp(
      SwaggerInterpreter(
        openAPIInterpreterOptions = openApiDocsConfig,
        swaggerUIOptions = SwaggerUIOptions.default.pathPrefix(List("project-name-default", "docs"))
      )
        .fromEndpoints[Task](
          HealthEndpoint.endpointsSpecs,
          "Project Default",
          "v1"
        )
    )
}
