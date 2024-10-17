package br.com.caju.projectnamedefault.lib.transactor

import doobie.util.{Get, Put}
import doobie.util.meta.TimeMeta
import enumeratum.{Enum, EnumEntry}
import zio.json.ast.Json
import zio.json._

import java.time.{LocalDate, LocalDateTime}
import java.util.UUID
import scala.util.Try

trait SqlConversions extends TimeMeta {
  implicit val putUUID: Put[UUID] = Put[String].contramap(_.toString())
  implicit val getUUID: Get[UUID] = Get[String].temap(v => Try(UUID.fromString(v)).toEither.left.map(_.getMessage()))

  implicit val getDateTime: Get[LocalDateTime] =
    doobie.implicits.javatimedrivernative.JavaTimeLocalDateTimeMeta.get
  implicit val putDateTime: Put[LocalDateTime] =
    Put[String].contramap(_.toString())

  implicit val getDate: Get[LocalDate] =
    doobie.implicits.javatimedrivernative.JavaTimeLocalDateMeta.get
  implicit val putDate: Put[LocalDate] = Put[String].contramap(_.toString)

  def putEnum[E <: EnumEntry]: Put[E] = Put[String].contramap(_.entryName)
  def getEnum[E <: EnumEntry](implicit e: Enum[E]): Get[E] =
    Get[String].temap(str =>
      e.withNameInsensitiveOption(str)
        .toRight(s"Could not find ${e.getClass} with name $str")
    )

  implicit val putJson: Put[Json] = Put[String].contramap(a => a.toString)
  implicit val getJson: Get[Json] = Get[String].temap { jsonStr =>
    jsonStr
      .fromJson[Json]
      .left
      .map(e => s"Cannot parse json content $jsonStr. Error: $e")
  }
}
