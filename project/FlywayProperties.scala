import io.github.davidmweber.FlywayPlugin.autoImport.{
  flywayLocations,
  flywayPassword,
  flywayUrl,
  flywayUser
}

import scala.util.Properties.envOrElse

object FlywayProperties {

  val default = Seq(
    flywayUrl := envOrElse(
      "FLYWAY_URL",
      "jdbc:mysql://127.0.0.1:3306/project_name_default"
    ),
    flywayUser := envOrElse("FLYWAY_USER", "root"),
    flywayPassword := envOrElse("FLYWAY_PASSWORD", "root"),
    flywayLocations := Seq(
      "filesystem:" + envOrElse(
        "FLYWAY_LOCATIONS",
        "adapter/src/main/resources/migration/"
      )
    )
  )
}
