name := "project-name-default"
ThisBuild / scalaVersion := "2.13.14"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "br.com.caju"
ThisBuild / organizationName := "Caju Benefícios"
ThisBuild / packageDoc / publishArtifact := false
ThisBuild / testFrameworks := Seq(
  new TestFramework("zio.test.sbt.ZTestFramework")
)

ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixOnCompile := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

ThisBuild / scalacOptions ++= CompilerOptions.options

ThisBuild / coverageExcludedPackages := "<empty>;.*web.infra.*;.*web.utils.*;.*web.session.*;.*common.*;.*app.*;.*consumer.infra*;.*consumer.messaging*;.*lib.*;.*database.infra.*;.*service.infra.*;.*adapter.infra.*;.*adapter.messaging.encoder.*"
ThisBuild / coverageExcludedFiles := ".*package.*;.*Main.*;.*DocsEndpoint.*;.*Topics.*"

GithubPackages.tokenResolutionSource
ThisBuild / resolvers += Resolver.githubPackages("caju-beneficios")

fork := true
Test / fork := true
Test / parallelExecution := false

lazy val common = (project in file("common"))
  .settings(Settings.commonDependencies)

lazy val lib = (project in file("lib"))
  .settings(Settings.libDependencies)

lazy val commonTest = (project in file("commontest"))
  .dependsOn(lib, common)
  .settings(Settings.commonTestDependencies)

lazy val core = (project in file("core"))
  .dependsOn(lib, common, commonTest % Test)
  .settings(Settings.coreDependencies)

lazy val adapter = (project in file("adapter"))
  .dependsOn(lib, core, common, commonTest % Test)
  .settings(GithubPackages.tokenResolutionSource)
  .settings(Settings.adapterDependencies)
  .enablePlugins(FlywayPlugin)
  .settings(FlywayProperties.default)

lazy val web = (project in file("web"))
  .dependsOn(lib, core, adapter, common, commonTest % Test)
  .aggregate(core, adapter, common)
  .settings(Settings.webDependencies, Settings.agentsDependencies)
  .enablePlugins(JavaAppPackaging, JavaAgent)

lazy val consumer = (project in file("consumer"))
  .dependsOn(lib, core, adapter, common, commonTest % Test)
  .aggregate(core, adapter, common)
  .settings(Settings.consumerDependencies, Settings.agentsDependencies)
  .settings(
    Compile / mainClass := Some("br.com.caju.projectnamedefault.consumer.ConsumerMain")
  )
  .enablePlugins(JavaAppPackaging, JavaAgent)

lazy val app = (project in file("app"))
  .dependsOn(web, consumer)
  .aggregate(web, consumer)
  .settings(GithubPackages.tokenResolutionSource, Settings.agentsDependencies)
  .settings(FlywayProperties.default)
  .enablePlugins(JavaAppPackaging, JavaAgent, FlywayPlugin)

lazy val runApp = taskKey[Unit]("run app")
runApp := (app / Compile / runMain).toTask(" br.com.caju.projectnamedefault.Main").value

lazy val runConsumers = taskKey[Unit]("run consumers")
runConsumers := (app / Compile / runMain)
  .toTask(" br.com.caju.projectnamedefault.ConsumerMain")
  .value
