package br.com.caju.projectnamedefault.health

import br.com.caju.projectnamedefault.Mocks.MockCheckingDatabase
import br.com.caju.projectnamedefault.core.health.CheckHealth
import br.com.caju.projectnamedefault.core.health.driver.CheckingHealth
import io.github.gaelrenoux.tranzactio.doobie.Database
import zio.Scope
import zio.mock.{Expectation, MockSpecDefault}
import zio.test.Assertion.{isLeft, isRight}
import zio.test.{Spec, TestAspect, TestEnvironment, assertZIO}

object CheckHealthSpec extends MockSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("CheckHealthSpec")(
      suite("check")(check1, check2)
    ) @@ TestAspect.silentLogging

  private val check1 = test("should get success") {
    val checkingDatabase =
      MockCheckingDatabase.IsHealthy(Expectation.value(true))

    val layer = Database.none
      .++(checkingDatabase) >>> CheckHealth.layer

    val result = CheckingHealth
      .check(true)
      .provideLayer(layer)

    assertZIO(result.either)(isRight)
  }

  private val check2 = test("Should get error") {
    val checkingDatabase =
      MockCheckingDatabase.IsHealthy(
        Expectation.failure(
          new Throwable("Error when checking database connection")
        )
      )

    val layer = Database.none
      .++(checkingDatabase) >>> CheckHealth.layer

    val result = CheckingHealth
      .check(true)
      .provideLayer(layer)

    assertZIO(result.either)(isLeft)
  }
}
