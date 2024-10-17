package br.com.caju.projectnamedefault.web.endpoints

import br.com.caju.projectnamedefault.adapter.persistence.HealthRepository
import br.com.caju.projectnamedefault.core.health.CheckHealth
import br.com.caju.projectnamedefault.core.health.driver.{CheckHealthData, CheckingHealth}
import br.com.caju.projectnamedefault.web.infra.{IntegrationSpec, TestContainerLayer}
import br.com.caju.projectnamedefault.web.infra.TestContainerLayer.mysqlSettingsLayer
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.github.scottweaver.zio.testcontainers.mysql.ZMySQLContainer
import sttp.client3.basicRequest
import sttp.model.StatusCode
import zio.test.TestAspect.sequential
import zio.test._
import zio.{Scope => ZIOScope}

object HealthEndpointSpec extends IntegrationSpec {

  override def spec: Spec[TestEnvironment with ZIOScope, Any] =
    suite("HealthEndpointSpec")(
      suite("checkIfIsHealthy")(healthEndpointCase1) @@ sequential
    ).provideShared(
      TestContainerLayer.tZio,
      HealthRepository.layer,
      ZMySQLContainer.live,
      CheckHealth.layer,
      mysqlSettingsLayer
    )

  private lazy val healthEndpointCase1 =
    test("Should response with success when is healthy") {
      val backendStub = stubDriverInterpreter[CheckingHealth]
        .whenServerEndpointRunLogic(HealthEndpoint.healthCheckEndpoint)
        .backend()

      implicit val healthCheckResponseDecoder: Decoder[CheckHealthData] =
        deriveDecoder

      val execution = for {
        response <- basicRequest
          .get(projectUri("health")(""))
          .send(backendStub)
        body = parseSuccessResponse[CheckHealthData](response)
      } yield assertTrue(
        response.code == StatusCode.Ok,
        body.database.isDefined,
        body.uptime.isValidLong
      )

      assertAndRollback(execution)
    }
}
