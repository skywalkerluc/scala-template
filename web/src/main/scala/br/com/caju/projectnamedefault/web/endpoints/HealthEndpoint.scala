package br.com.caju.projectnamedefault.web.endpoints

import br.com.caju.projectnamedefault.core.health.driver.{CheckHealthData, CheckingHealth}
import br.com.caju.projectnamedefault.web.endpoints.input.HealthEndpointInput
import br.com.caju.projectnamedefault.web.endpoints.output.HealthEndpointOutput
import sttp.tapir.query
import sttp.tapir.ztapir.RichZEndpoint

object HealthEndpoint extends HealthEndpointInput with HealthEndpointOutput {

  private lazy val getHealthCheck =
    healthRoute.get
      .in(query[Option[Boolean]]("check_db"))
      .out(jsonBody[CheckHealthData])
      .errorOut(outputCheckingDatabaseError)

  val endpointsSpecs = List(getHealthCheck)

  def healthCheckEndpoint = {
    def healthCheckLogic(checkDatabase: Option[Boolean]) =
      CheckingHealth
        .check(checkDatabase.getOrElse(true))
        .mapError(checkingDatabaseError.handleErrors)

    getHealthCheck.zServerLogic(healthCheckLogic)
  }

  val endpoints = toHttp(healthCheckEndpoint)
}
