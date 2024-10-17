package br.com.caju.projectnamedefault.web.infra

import br.com.caju.projectnamedefault.adapter.infra.Environment.{AdapterEnvironment, adapterEnvironment}
import br.com.caju.projectnamedefault.core.health.CheckHealth
import br.com.caju.projectnamedefault.core.health.driver.CheckingHealth
import br.com.caju.projectnamedefault.lib.logger.LoggerUtils
import br.com.caju.projectnamedefault.lib.zuuid.ZUUID
import io.github.gaelrenoux.tranzactio.doobie.{Database => Tx}
import zio.{ULayer, ZLayer}

import javax.sql.DataSource

object Environment {

  type WebEnvironment = WebConfig with AdapterEnvironment with ZUUID with CheckingHealth

  private val config: ULayer[WebConfig] =
    ZLayer.fromZIO(ConfigLoader.load).orDie
  private val hikariTransactor = config >>> TransactorFactory.layer
  private val tzioDatabase = hikariTransactor.project(
    _.kernel.asInstanceOf[DataSource]
  ) >>> Tx.fromDatasource

  val webEnvironment = ZLayer.make[WebEnvironment](
    config,
    tzioDatabase,
    adapterEnvironment,
    LoggerUtils.layer,
    ZUUID.layer,
    // Use cases
    CheckHealth.layer
  )
}
