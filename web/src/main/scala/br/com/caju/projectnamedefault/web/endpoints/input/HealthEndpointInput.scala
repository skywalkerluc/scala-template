package br.com.caju.projectnamedefault.web.endpoints.input

import br.com.caju.projectnamedefault.web.codecs.HealthCodecs
import br.com.caju.projectnamedefault.web.endpoints.rootEndpoint

trait HealthEndpointInput extends HealthCodecs {
  lazy val healthRoute = rootEndpoint.in("health").tag("Health")

}
