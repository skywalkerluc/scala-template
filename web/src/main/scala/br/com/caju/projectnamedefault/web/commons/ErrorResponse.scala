package br.com.caju.projectnamedefault.web.commons

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder, JsonObject}

case class ErrorResponse(
    errorKey: String,
    message: String,
    data: Option[JsonObject] = None
)

object ErrorResponse {

  implicit val selfEnc: Encoder[ErrorResponse] = deriveEncoder
  implicit val selfDec: Decoder[ErrorResponse] = deriveDecoder

  def apply(errorKey: String, message: String, data: JsonObject) =
    new ErrorResponse(errorKey, message, Some(data))

  /** Generic forbidden error response
    */
  val forbidden: ErrorResponse = ErrorResponse("FORBIDDEN", "Forbidden access")

  /** Generic unauthorized error response
    */
  val unauthorized: ErrorResponse =
    ErrorResponse("UNAUTHORIZED", "Unauthorized access")

  /** Request parsing failure error response
    */
  val parsingFailure: ErrorResponse =
    ErrorResponse("PARSING_FAILURE", "Request body parsing failed")

  /** Generic dependency failure error response
    */
  val dependencyFailure: ErrorResponse =
    ErrorResponse(
      "DEPENDENCY_FAILURE",
      "Internal component failed while handling request. Please try again later."
    )

  val tokenExpired: ErrorResponse =
    ErrorResponse("TOKEN_EXPIRED", "Token's expired")

  val notFound: ErrorResponse = ErrorResponse("NOT_FOUND", "Resource not found")

}
