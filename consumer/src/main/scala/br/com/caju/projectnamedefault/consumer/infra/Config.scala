package br.com.caju.projectnamedefault.consumer.infra

import zio.config.magnolia.deriveConfig
import zio.config.typesafe.FromConfigSourceTypesafe
import zio.{ConfigProvider, Task}

case class ConsumerConfig(
    database: DatabaseConfig,
    kafka: KafkaConfig
)

case class DatabaseConfig(
    driver: String,
    url: String,
    user: String,
    password: String,
    threadPoolSize: Int,
    maxPoolSize: Int,
    minimumIdle: Int,
    poolName: String
)

case class KafkaConfig(hosts: String) {
  def getHosts: List[String] = hosts.split(",").toList
}

object ConfigLoader {

  val load: Task[ConsumerConfig] =
    ConfigProvider
      .fromResourcePath()
      .load(deriveConfig[ConsumerConfig])
}
