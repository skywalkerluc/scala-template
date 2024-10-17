package br.com.caju.projectnamedefault.infra

import io.github.gaelrenoux.tranzactio.doobie.Database
import zio.test.{TestResult, ZIOSpecDefault}
import zio.{RIO, ZIO}

trait IntegrationSpec extends ZIOSpecDefault {

  def assertAndRollback[R <: Database](
      test: RIO[R, TestResult]
  ): ZIO[R with Database, Nothing, TestResult] =
    Database
      .transactionOrDie[R, TestResult, Nothing](test.orDie.flip)
      .flip
}
