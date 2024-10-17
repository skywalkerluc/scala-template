package br.com.caju.projectnamedefault.consumer.infra

import br.com.caju.kafkacommons.factory.BrokerConnection
import br.com.caju.projectnamedefault.consumer.infra.kafka.NotifierLayers
import br.com.caju.projectnamedefault.lib.transactor.ZHikariTransactor._
import com.dimafeng.testcontainers.MySQLContainer
import io.github.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import io.github.gaelrenoux.tranzactio.doobie.{Database => Tx}
import io.github.scottweaver.models.JdbcInfo
import io.github.scottweaver.zio.testcontainers.mysql.ZMySQLContainer.Settings
import zio.kafka.consumer.ConsumerSettings
import zio.kafka.embedded.Kafka
import zio.kafka.embedded.Kafka.EmbeddedKafkaService
import zio.{ULayer, ZIO, ZLayer}

import java.net.ServerSocket
import javax.sql.DataSource

object TestContainerLayer extends NotifierLayers {
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

  val mysqlSettingsLayer: ULayer[Settings] = ZLayer.succeed(
    Settings(
      "8.2.0",
      "project-name-default-test",
      MySQLContainer.defaultUsername,
      MySQLContainer.defaultPassword
    )
  )

  val tZio = dataSourceLayer.project(_.kernel.asInstanceOf[DataSource]) >>> Tx.fromDatasource

  val kafkaClient: ZLayer[Any, Throwable, Kafka] = ZLayer.scoped {
    def getAvailablePort = {
      val socket = new ServerSocket(0)
      val port   = socket.getLocalPort
      socket.close()
      port
    }

    implicit val embeddedKafkaConfig: EmbeddedKafkaConfig = EmbeddedKafkaConfig(
      kafkaPort = getAvailablePort,
      zooKeeperPort = getAvailablePort,
      customBrokerProperties = Map("group.min.session.timeout.ms" -> "500", "group.initial.rebalance.delay.ms" -> "0")
    )
    ZIO.acquireRelease(ZIO.attempt(EmbeddedKafkaService(EmbeddedKafka.start())))(_.stop())
  }

  val kafka: ZLayer[Any, Nothing, Kafka] = kafkaClient.orDie

  val brokerLayer: ZLayer[Any, Any, BrokerConnection]   = kafka >>> brokerConnectionLayer
  val consumerLayer: ZLayer[Any, Any, ConsumerSettings] = kafka >>> consumerSettingsLayer
}
