package br.com.caju.projectnamedefault.web.infra

import br.com.caju.projectnamedefault.lib.transactor.ZHikariTransactor._
import com.dimafeng.testcontainers.MySQLContainer
import io.github.gaelrenoux.tranzactio.doobie.{Database => Tx}
import io.github.scottweaver.models.JdbcInfo
import io.github.scottweaver.zio.testcontainers.mysql.ZMySQLContainer.Settings
import zio.{ZIO, ZLayer}

import javax.sql.DataSource

object TestContainerLayer {
  val dataSourceLayer = ZLayer.scoped {
    ZIO.service[JdbcInfo].flatMap { jdbcInfo =>
      val transactorConnection = TransactorConnection(
        driver = jdbcInfo.driverClassName,
        url = jdbcInfo.jdbcUrl,
        user = jdbcInfo.username,
        password = jdbcInfo.password,
        threadPoolSize = 10,
        maxPoolSize = 10,
        minimumIdle = 5,
        poolName = "Test_Container"
      )
      makeHikari(transactorConnection)
    }
  }

  val mysqlSettingsLayer = ZLayer.succeed(
    Settings(
      "8.2.0",
      "project-name-default-test",
      MySQLContainer.defaultUsername,
      MySQLContainer.defaultPassword
    )
  )

  val tZio = dataSourceLayer.project(
    _.kernel.asInstanceOf[DataSource]
  ) >>> Tx.fromDatasource
}
