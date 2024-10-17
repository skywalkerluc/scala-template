package br.com.caju.projectnamedefault.core.health

import br.com.caju.projectnamedefault.core.health.driver.{CheckHealthData, CheckHealthError, CheckingHealth}
import br.com.caju.projectnamedefault.core.driven.CheckingDatabase
import io.github.gaelrenoux.tranzactio.doobie.Database
import zio.{IO, ZIO, ZLayer}

import java.lang.management.ManagementFactory

case class CheckHealth(checkingDatabase: CheckingDatabase, db: Database.Service) extends CheckingHealth {
  override def check(
      shouldCheckDatabase: Boolean
  ): IO[CheckHealthError, CheckHealthData] =
    for {
      uptime   <- getRuntime
      database <- checkDatabaseOrIgnore(db, shouldCheckDatabase)
    } yield CheckHealthData(uptime, database)

  private def getRuntime =
    ZIO.succeed(ManagementFactory.getRuntimeMXBean.getUptime)

  private def checkDatabaseOrIgnore(
      db: Database.Service,
      shouldCheckDatabase: Boolean
  ) =
    db.autoCommit(checkingDatabase.isHealthy.as(true))
      .when(shouldCheckDatabase)
      .orElseFail(CheckHealthError.DBError)
}

object CheckHealth {
  val layer =
    ZLayer {
      for {
        cd <- ZIO.service[CheckingDatabase]
        db <- ZIO.service[Database.Service]
      } yield CheckHealth(cd, db)
    }
}
