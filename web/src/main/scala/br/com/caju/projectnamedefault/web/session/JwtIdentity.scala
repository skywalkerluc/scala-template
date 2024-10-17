package br.com.caju.projectnamedefault.web.session

import cats.implicits.toFunctorOps
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

import java.util.UUID

sealed trait JwtIdentity

object JwtIdentity {
  implicit val decoder: Decoder[JwtIdentity] =
    List[Decoder[JwtIdentity]](
      AdminJwt.decoder.widen
    ).reduce(_ or _)
}

case class AdminJwt(
    userId: UUID,
    admin: AdminJwt.Profile
) extends JwtIdentity

object AdminJwt {
  case class Profile(adminId: UUID, role: String)

  implicit val profileDec: Decoder[AdminJwt.Profile] = deriveDecoder
  implicit val profileEnc: Encoder[AdminJwt.Profile] = deriveEncoder
  implicit val decoder: Decoder[AdminJwt]            = deriveDecoder
  implicit val encoder: Encoder[AdminJwt]            = deriveEncoder
}
