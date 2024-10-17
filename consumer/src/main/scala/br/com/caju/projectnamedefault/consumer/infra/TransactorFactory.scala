package br.com.caju.projectnamedefault.consumer.infra

import br.com.caju.projectnamedefault.lib.transactor.ZHikariTransactor.{TransactorConnection, makeHikari}
import io.scalaland.chimney.dsl.TransformerOps
import zio.{ZIO, ZLayer}

object TransactorFactory {
  val layer = ZLayer.scoped {
    ZIO
      .service[ConsumerConfig]
      .flatMap(config => makeHikari(config.database.transformInto[TransactorConnection]))
  }
}
