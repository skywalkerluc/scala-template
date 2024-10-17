import sbt.*

object Dependencies {
  // ZIO
  private val zioVersion            = "2.1.7"
  private val zioHttpVersion        = "3.0.0-RC8"
  private val zioInteropCatsVersion = "23.1.0.3"
  private val zioHttpCommonsVersion = "0.1.10"
  private val circeVersion          = "0.14.2"
  private val zioConfigVersion      = "4.0.2"

  // Logs
  private val zioLoggingVersion      = "2.1.13"
  private val logbackAppenderVersion = "1.5.6"
  private val logstashEncoderVersion = "7.4"

  // Kafka
  private val zkafkaCommonsVersion = "0.4.3-zio-2"

  // Commons
  private val commonsLangVersion = "3.12.0"
  private val chimneyVersion     = "0.8.5"
  private val enumeratumVersion  = "1.7.4"
  private val sttpVersion        = "3.9.5"

  // Database
  private val doobieVersion        = "1.0.0-RC5"
  private val doobieHelpersVersion = "0.3.0"
  private val mysqlVersion         = "8.0.33"
  private val flywayVersion        = "10.11.1"
  private val tranzactIOVersion    = "5.2.0"

  // HTTP Commons
  private val tapirVersion    = "1.11.1"
  private val authzVersion    = "1.4.5"
  private val jwtCirceVersion = "9.0.6"

  // Test
  private val testContainersVersion = "0.10.0"
  private val zioMockVersion        = "1.0.0-RC11"
  private val zioKafkaUtilsVersion  = "2.0.2"

  // Agents version
  private val dataDoghqVersion = "1.35.0"

  lazy private val zio       = "dev.zio" %% "zio"        % zioVersion
  lazy private val zioMacros = "dev.zio" %% "zio-macros" % zioVersion
  lazy private val zioInteropCats =
    "dev.zio" %% "zio-interop-cats" % zioInteropCatsVersion
  lazy private val zioLogging = "dev.zio" %% "zio-logging" % zioLoggingVersion
  lazy private val ziohttp    = "dev.zio" %% "zio-http"    % zioHttpVersion
  lazy private val zioLoggingSlf4j =
    "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion
  lazy private val circeCore    = "io.circe" %% "circe-core"    % circeVersion
  lazy private val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  lazy private val circeExtra =
    "io.circe" %% "circe-generic-extras" % circeVersion
  lazy private val circeParser = "io.circe" %% "circe-parser" % circeVersion
  lazy private val jwtCirce =
    "com.github.jwt-scala" %% "jwt-circe" % jwtCirceVersion
  lazy private val sttpCirce =
    "com.softwaremill.sttp.client3" %% "circe" % sttpVersion
  lazy private val mysql      = "mysql"         % "mysql-connector-java" % mysqlVersion
  lazy private val doobieCore = "org.tpolecat" %% "doobie-core"          % doobieVersion
  lazy private val doobieHikari =
    "org.tpolecat" %% "doobie-hikari" % doobieVersion
  lazy private val chimney = "io.scalaland" %% "chimney" % chimneyVersion
  lazy private val enumeratum =
    "com.beachape" %% "enumeratum" % enumeratumVersion
  lazy private val enumeratumCirce =
    "com.beachape" %% "enumeratum-circe" % enumeratumVersion
  lazy private val tranzactio =
    "io.github.gaelrenoux" %% "tranzactio-doobie" % tranzactIOVersion
  lazy private val doobieHelpers =
    "br.com.caju" %% "doobie-helpers" % doobieHelpersVersion
  lazy private val tapirZioHttp    = "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server"   % tapirVersion
  lazy private val tapirSwagger    = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion
  lazy private val tapirEnumeratum = "com.softwaremill.sttp.tapir" %% "tapir-enumeratum"        % tapirVersion
  lazy private val tapirJsonCirce  = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"        % tapirVersion

  lazy private val zioConfig         = "dev.zio" %% "zio-config"          % zioConfigVersion
  lazy private val zioConfigTypeSafe = "dev.zio" %% "zio-config-typesafe" % zioConfigVersion
  lazy private val zioConfigMagnolia = "dev.zio" %% "zio-config-magnolia" % zioConfigVersion
  lazy private val zioConfigRefined  = "dev.zio" %% "zio-config-refined"  % zioConfigVersion

  lazy private val logbackAppender =
    "ch.qos.logback" % "logback-classic" % logbackAppenderVersion
  lazy private val logstashEncoder =
    "net.logstash.logback" % "logstash-logback-encoder" % logstashEncoderVersion
  lazy private val asyncSttpClient =
    "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % sttpVersion
  lazy private val flyway      = "org.flywaydb" % "flyway-core"  % flywayVersion
  lazy private val flywayMysql = "org.flywaydb" % "flyway-mysql" % flywayVersion
  lazy private val zkafkaCommons =
    "br.com.caju" %% "zkafka-commons" % zkafkaCommonsVersion
  lazy private val commonsLang =
    "org.apache.commons" % "commons-lang3" % commonsLangVersion

  // agents
  lazy private val dataDogJavaAgent =
    "com.datadoghq" % "dd-java-agent" % dataDoghqVersion % "compile;runtime"

  // test
  lazy val zioTest     = "dev.zio" %% "zio-test"     % zioVersion
  lazy val zioTestSbt  = "dev.zio" %% "zio-test-sbt" % zioVersion     % Test
  lazy val zioTestMock = "dev.zio" %% "zio-mock"     % zioMockVersion % Test
  lazy val tapirStubServer =
    "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test
  lazy val zioTestMagnolia =
    "dev.zio" %% "zio-test-magnolia" % zioVersion % Test
  lazy val zioKafkaTestUtils =
    "dev.zio" %% "zio-kafka-test-utils" % zioKafkaUtilsVersion
  lazy val testContainersMysql =
    "io.github.scottweaver" %% "zio-2-0-testcontainers-mysql" % testContainersVersion
  lazy val testDbMigrationAspect =
    "io.github.scottweaver" %% "zio-2-0-db-migration-aspect" % testContainersVersion

  val appAgents = Seq(dataDogJavaAgent)

  private val sharedDeps = Seq(
    zio,
    zioMacros,
    zioInteropCats,
    zioConfig,
    zioConfigTypeSafe,
    zioConfigMagnolia,
    zioConfigRefined,
    chimney,
    zioLogging,
    zioLoggingSlf4j,
    logbackAppender,
    logstashEncoder,
    enumeratum,
    zioKafkaTestUtils % Test,
    zioTest           % Test,
    zioTestSbt,
    zioTestMock,
    zioTestMagnolia,
    tapirStubServer
  )

  val commonTestDeps = sharedDeps ++ Seq(zioKafkaTestUtils, zkafkaCommons, testContainersMysql, testDbMigrationAspect)

  val libDeps = sharedDeps ++ Seq(
    tapirZioHttp,
    enumeratumCirce,
    doobieHelpers,
    doobieCore,
    doobieHikari,
    zioTest,
    sttpCirce,
    asyncSttpClient
  )

  val coreDeps = sharedDeps ++ Seq(
    tranzactio,
    enumeratumCirce,
    commonsLang
  )

  val adapterDeps = sharedDeps ++ Seq(
    circeCore,
    circeGeneric,
    circeExtra,
    circeParser,
    enumeratumCirce,
    doobieCore,
    doobieHikari,
    tranzactio,
    zkafkaCommons,
    doobieHelpers,
    flyway,
    flywayMysql,
    testContainersMysql   % Test,
    testDbMigrationAspect % Test,
    testDbMigrationAspect,
    zioTest,
    asyncSttpClient,
    tapirEnumeratum,
    circeGeneric,
    sttpCirce
  )

  val consumerDeps = sharedDeps ++ Seq(
    circeCore,
    circeGeneric,
    circeExtra,
    circeParser,
    doobieHelpers,
    doobieCore,
    doobieHikari,
    chimney,
    mysql,
    tranzactio,
    logbackAppender,
    logstashEncoder,
    zioLogging,
    zioLoggingSlf4j
  )

  val webDeps = sharedDeps ++ Seq(
    ziohttp,
    tapirZioHttp,
    tapirSwagger,
    circeCore,
    circeGeneric,
    circeExtra,
    circeParser,
    sttpCirce,
    jwtCirce,
    doobieHelpers,
    mysql,
    doobieCore,
    doobieHikari,
    tranzactio,
    tapirJsonCirce,
    tapirStubServer,
    testContainersMysql   % Test,
    testDbMigrationAspect % Test
  )

  val commonDeps = sharedDeps ++ Seq(
    tapirZioHttp,
    tapirSwagger,
    circeCore,
    circeExtra,
    circeParser,
    jwtCirce,
    doobieHelpers,
    mysql,
    doobieCore,
    doobieHikari,
    tranzactio,
    enumeratumCirce
  )
}
