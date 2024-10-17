package br.com.caju.projectnamedefault.web.infra

import doobie.util.transactor
import io.circe.Decoder
import io.circe.parser._
import io.github.gaelrenoux.tranzactio.DatabaseOps
import io.github.gaelrenoux.tranzactio.doobie.Database
import io.github.scottweaver.models.JdbcInfo
import io.github.scottweaver.zio.testcontainers.mysql.ZMySQLContainer
import io.github.scottweaver.zio.testcontainers.mysql.ZMySQLContainer.Settings
import sttp.client3.Response
import sttp.client3.testing.SttpBackendStub
import sttp.model.Uri
import sttp.tapir.server.stub.TapirStubInterpreter
import sttp.tapir.server.ziohttp.ZioHttpServerOptions
import sttp.tapir.ztapir.RIOMonadError
import zio.test.{TestResult, ZIOSpecDefault}
import zio.{RIO, Task, ZIO, ZLayer}

trait IntegrationSpec extends ZIOSpecDefault {
  private val endpoint = "http://test.com"

  def projectUri(uri: String)(implicit version: String = "v1") = {
    val uriFull = version match {
      case str if str.isEmpty => s"$endpoint/project-name-default/$uri"
      case _                  => s"$endpoint/project-name-default/$version/$uri"
    }
    Uri.parse(uriFull) match {
      case Right(uri) => uri
      case Left(e)    => throw new RuntimeException(s"Can't parse uri. Error: $e")
    }
  }

  def parseSuccessResponse[A: Decoder](
      response: Response[Either[String, String]]
  ): A =
    (for {
      jsonBody <- response.body
      decodedBody <- decode[A](jsonBody)
    } yield decodedBody)
      .getOrElse(throw new RuntimeException("Can't parse success response."))

  def stubDriverInterpreter[A] =
    TapirStubInterpreter(
      ZioHttpServerOptions.customiseInterceptors[A],
      SttpBackendStub.apply(new RIOMonadError[A])
    )

  val defaultLayers =
    ZLayer.make[Settings with DatabaseOps.ServiceOps[
      transactor.Transactor[Task]
    ] with JdbcInfo](
      TestContainerLayer.mysqlSettingsLayer,
      TestContainerLayer.tZio,
      ZMySQLContainer.live
    )

  def assertAndRollback[R <: Database](
      test: RIO[R, TestResult]
  ): ZIO[R with Database, Nothing, TestResult] =
    Database
      .transactionOrDie[R, TestResult, Nothing](test.orDie.flip)
      .flip
}
