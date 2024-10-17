package br.com.caju.projectnamedefault.web.infra

import br.com.caju.projectnamedefault.lib.transactor.ZHikariTransactor.TransactorConnection
import br.com.caju.projectnamedefault.lib.transactor.ZHikariTransactor
import io.scalaland.chimney.dsl.TransformerOps
import zio.{ZIO, ZLayer}

object TransactorFactory {
  val layer = ZLayer.scoped {
    ZIO
      .service[WebConfig]
      .flatMap(config =>
        ZHikariTransactor.makeHikari(
          config.database.transformInto[TransactorConnection]
        )
      )
  }
}
