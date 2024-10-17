package br.com.caju.projectnamedefault

import br.com.caju.projectnamedefault.adapter.persistence.HealthRepository
import br.com.caju.projectnamedefault.core.driven.CheckingDatabase
import br.com.caju.projectnamedefault.infra.TestContainerLayer.mysqlSettingsLayer
import br.com.caju.projectnamedefault.infra.{IntegrationSpec, TestContainerLayer}
import io.github.scottweaver.zio.testcontainers.mysql.ZMySQLContainer
import zio.test.TestAspect.sequential
import zio.test.{Spec, TestEnvironment, assertTrue}
import zio.{Scope, ZIO}

object HealthRepositorySpec extends IntegrationSpec {

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("HealthRepositorySpec")(
      suite("checkIfIsHealthy")(isHealthy1) @@ sequential
    ).provideShared(
      TestContainerLayer.dataSourceLayer,
      TestContainerLayer.tZio,
      HealthRepository.layer,
      ZMySQLContainer.live,
      mysqlSettingsLayer
    )

  private val isHealthy1 = test("should succeed when database is healthy") {
    val assertion = for {
      checkingDatabase <- ZIO.service[CheckingDatabase]
      output <- checkingDatabase.isHealthy.as(true)
    } yield assertTrue(output)

    assertAndRollback(assertion)
  }
}
