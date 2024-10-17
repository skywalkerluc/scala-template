package br.com.caju.projectnamedefault.adapter.persistence

import br.com.caju.projectnamedefault.core.driven.CheckingDatabase
import br.com.caju.projectnamedefault.lib.logger.LoggerUtils.ZIOLogOps
import doobie.implicits.toSqlInterpolator
import io.github.gaelrenoux.tranzactio.doobie.{Connection, tzio}
import zio.{ULayer, ZIO, ZLayer}

case class HealthRepository() extends CheckingDatabase {
  override def isHealthy: ZIO[Connection, Throwable, Boolean] =
    tzio(sql"SELECT 1".query[Int].unique)
      .tapError(e =>
        ZIO
          .logError("Error when checking database connection")
          .withLogAnnotation("error", e.toString)
      )
      .orDie
      .isSuccess

}

object HealthRepository {
  val layer: ULayer[HealthRepository] = ZLayer.succeed(HealthRepository())
}
