package br.com.caju.projectnamedefault.consumer.infra

import br.com.caju.projectnamedefault.consumer.infra.kafka.TestHelpers
import io.github.gaelrenoux.tranzactio.doobie.Database
import io.github.scottweaver.zio.aspect.DbMigrationAspect
import sttp.client3.testing.SttpBackendStub
import sttp.tapir.server.stub.TapirStubInterpreter
import sttp.tapir.server.ziohttp.ZioHttpServerOptions
import sttp.tapir.ztapir.RIOMonadError
import zio.test.{TestResult, ZIOSpecDefault}
import zio.{RIO, ZIO}

trait ConsumerSpec extends ZIOSpecDefault with TestHelpers {

  object IAspect {
    val migrate = DbMigrationAspect.migrateOnce(
      s"filesystem:adapter/src/main/resources/migration"
    )()
  }
  def stubDriverInterpreter[A] =
    TapirStubInterpreter(
      ZioHttpServerOptions.customiseInterceptors[A],
      SttpBackendStub.apply(new RIOMonadError[A])
    )

  def assertAndRollback[R <: Database](
      test: RIO[R, TestResult]
  ): ZIO[R with Database, Nothing, TestResult] =
    Database
      .transactionOrDie[R, TestResult, Nothing](test.orDie.flip)
      .flip
}
