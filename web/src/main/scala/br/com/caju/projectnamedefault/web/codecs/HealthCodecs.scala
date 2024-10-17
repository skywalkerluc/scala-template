package br.com.caju.projectnamedefault.web.codecs

import br.com.caju.projectnamedefault.core.health.driver.CheckHealthData
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

trait HealthCodecs {
  implicit val healthDecoder: Encoder[CheckHealthData] = deriveEncoder
  implicit val healthEncoder: Decoder[CheckHealthData] = deriveDecoder
  implicit val healthSchema: Schema[CheckHealthData]   = Schema.derived
}
