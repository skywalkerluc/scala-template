package br.com.caju.projectnamedefault.web.endpoints.output

import br.com.caju.projectnamedefault.web.commons.ErrorResponseMapper
import br.com.caju.projectnamedefault.core.health.driver.CheckHealthError
import br.com.caju.projectnamedefault.web.codecs.ErrorResponseCodecs
import br.com.caju.projectnamedefault.web.commons.ErrorResponse
import sttp.model.StatusCode.ServiceUnavailable

trait HealthEndpointOutput extends ErrorResponseCodecs {
  val checkingDatabaseError: ErrorResponseMapper[CheckHealthError] =
    ErrorResponseMapper[CheckHealthError] { case CheckHealthError.DBError =>
      (ServiceUnavailable, ErrorResponse.dependencyFailure)
    }

  val outputCheckingDatabaseError =
    checkingDatabaseError.outputErrors(CheckHealthError.DBError)
}
