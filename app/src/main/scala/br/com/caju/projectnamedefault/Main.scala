package br.com.caju.projectnamedefault

import br.com.caju.projectnamedefault.web.endpoints.{HealthEndpoint, DocsEndpoint}
import br.com.caju.projectnamedefault.web.infra.WebConfig
import br.com.caju.projectnamedefault.web.infra.Environment.webEnvironment
import zio.{ZIO, ZIOAppDefault}
import zio.http.Server

object Main extends ZIOAppDefault {

  // no logs needed
  private val miscEndpoints =
    HealthEndpoint.endpoints.++(DocsEndpoint.endpoints)
  private def endpoints = miscEndpoints

  // Reference: https://patorjk.com/software/taag/#p=display&f=Big&t=Projeto%20Default
  private val art = """CREATE AN ART""".stripMargin

  override def run =
    (for {
      config <- ZIO.service[WebConfig]
      _ <- ZIO.log(
             s"$art Web main started! Docs: http://0.0.0.0:${config.http.port.toString}/project-name-default/docs"
           )
      app = endpoints

      _ <- Server
             .serve(app)
             .provide(Server.defaultWithPort(config.http.port), webEnvironment)
             .exitCode
    } yield ()).provide(webEnvironment)
}
