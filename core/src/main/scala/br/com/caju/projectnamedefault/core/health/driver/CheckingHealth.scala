package br.com.caju.projectnamedefault.core.health.driver

import zio.{IO, ZIO}

trait CheckingHealth {
  def check(shouldCheckDatabase: Boolean): IO[CheckHealthError, CheckHealthData]
}

object CheckingHealth {
  def check(
      shouldCheckDatabase: Boolean
  ): ZIO[CheckingHealth, CheckHealthError, CheckHealthData] =
    ZIO.serviceWithZIO(_.check(shouldCheckDatabase))
}

sealed trait CheckHealthError
object CheckHealthError {
  case object DBError extends CheckHealthError
}

case class CheckHealthData(uptime: Long, database: Option[Boolean])
