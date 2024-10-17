package br.com.caju.projectnamedefault.consumer.infra.kafka

import br.com.caju.kafkacommons.factory.BrokerConnection
import zio.{ZIO, ZLayer}
import zio.kafka.KafkaTestUtils
import zio.kafka.consumer.ConsumerSettings
import zio.kafka.embedded.Kafka
import java.util.UUID

trait NotifierLayers {
  def kafkaPrefix: String                 = this.getClass.getSimpleName.toLowerCase()
  def randomThing(prefix: String): String = s"$prefix-${UUID.randomUUID()}"
  def randomGroup: String                 = randomThing(s"$kafkaPrefix-group")
  def randomClient: String                = randomThing(s"$kafkaPrefix-client")

  val brokerConnectionLayer: ZLayer[Kafka, Nothing, BrokerConnection] = ZLayer.fromZIO {
    ZIO.serviceWith { (kafka: Kafka) =>
      BrokerConnection(kafka.bootstrapServers)
    }
  }

  val consumerSettingsLayer: ZLayer[Kafka, Nothing, ConsumerSettings] = ZLayer.fromZIO {
    KafkaTestUtils.consumerSettings(randomClient, Some(randomGroup))
  }
}
