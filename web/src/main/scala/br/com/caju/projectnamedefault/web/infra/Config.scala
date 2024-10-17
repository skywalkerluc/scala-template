package br.com.caju.projectnamedefault.web.infra

import zio.config.magnolia.deriveConfig
import zio.config.typesafe.FromConfigSourceTypesafe
import zio.{ConfigProvider, Task}

case class WebConfig(
    http: Http,
    database: DatabaseConfig
)

case class Http(port: Int)

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

object ConfigLoader {

  val load: Task[WebConfig] =
    ConfigProvider
      .fromResourcePath()
      .load(deriveConfig[WebConfig])
}
