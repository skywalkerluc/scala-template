package br.com.caju.projectnamedefault.web.commons

import br.com.caju.projectnamedefault.web.codecs.ErrorResponseCodecs
import sttp.model.StatusCode
import sttp.tapir.EndpointIO.Example
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.generic.Derived

object ErrorResponseMapper {
  def apply[T](
      mapper: T => (StatusCode, ErrorResponse)
  ): ErrorResponseMapper[T] = (input: T) => mapper(input)
}

trait ErrorResponseMapper[T] extends ErrorResponseCodecs {
  def apply(instance: T): (StatusCode, ErrorResponse)

  def handleErrors(instance: T): ErrorResponse = apply(instance)._2

  def outputErrors(
      headInstance: T,
      tailInstances: T*
  ): EndpointOutput.OneOf[ErrorResponse, ErrorResponse] = {
    val endpointOutputs = (headInstance :: tailInstances.toList)
      .groupBy(t => apply(t)._1)
      .toList
      .map { case (statusCode, instances) =>
        matchMultipleErrorResponses(statusCode, instances.map(apply(_)._2))
      }

    oneOf[ErrorResponse](endpointOutputs.head, endpointOutputs.tail: _*)
  }

  def matchMultipleErrorResponses(
      statusCode: StatusCode,
      errors: Seq[ErrorResponse]
  ) = {
    val errorKeys = errors.map(_.errorKey).toSet[String]

    implicit val customSchema =
      implicitly[Derived[Schema[ErrorResponse]]].value
        .modify(_.errorKey)(
          // to add enum
          _.validate(
            Validator.enumeration(errorKeys.toList, a => Some(a), None)
          )
        )
        .name(None)

    val examples = errors.map(body => Example(body, Some(body.errorKey), None))
    val body =
      jsonBody[ErrorResponse](
        errorResponseEncoder,
        errorResponseDecoder,
        customSchema
      ).examples(examples.toList)

    oneOfVariantValueMatcher(
      statusCode,
      body
    ) {
      case e: ErrorResponse if errorKeys.contains(e.errorKey) => true
    }
  }
}
