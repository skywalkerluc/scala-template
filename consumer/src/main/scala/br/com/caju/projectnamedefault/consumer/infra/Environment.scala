package br.com.caju.projectnamedefault.consumer.infra

//import br.com.caju.kafkacommons.Publisher
//import br.com.caju.kafkacommons.factory.{BrokerConnection, ConsumerFactory, ProducerFactory}
//import br.com.caju.projectnamedefault.adapter.infra.Environment.adapterEnvironment
import br.com.caju.projectnamedefault.lib.logger.LoggerUtils
//import io.github.gaelrenoux.tranzactio.doobie.{Database => Tx}
import zio.{ULayer, ZLayer}

//import javax.sql.DataSource

object Environment {

  private type ConsumerEnvironment = ConsumerConfig

  private val config: ULayer[ConsumerConfig] = ZLayer.fromZIO(ConfigLoader.load).orDie

//  private val brokerConnection = config.project(c => BrokerConnection(c.kafka.getHosts))
//  private val producerFactory = brokerConnection >>> ProducerFactory.layer
//  private val publisher = producerFactory >>> Publisher.layer

//  private val hikariTransactor = config.project(c => c.database) >>> TransactorFactory.layer
//  private val tzioDatabase = hikariTransactor.project(_.kernel.asInstanceOf[DataSource]) >>> Tx.fromDatasource

  lazy val consumersEnvironment =
    ZLayer.make[ConsumerEnvironment](
      config,
//      tzioDatabase,
//      brokerConnection,
//      publisher,
//      adapterEnvironment,
      LoggerUtils.layer
//      ConsumerFactory.layer
      // Consumers
      // Use cases
    )

}
