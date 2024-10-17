package br.com.caju.projectnamedefault.web.codecs

import br.com.caju.projectnamedefault.web.commons.ErrorResponse
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema
import sttp.tapir.json.circe.TapirJsonCirce

trait ErrorResponseCodecs extends TapirJsonCirce {
  implicit val errorResponseEncoder: Encoder[ErrorResponse] = deriveEncoder
  implicit val errorResponseDecoder: Decoder[ErrorResponse] = deriveDecoder
  implicit val errorResponseSchema: Schema[ErrorResponse]   = Schema.derived
}
