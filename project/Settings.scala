import com.lightbend.sbt.javaagent.JavaAgent.JavaAgentKeys.javaAgents
import sbt.Keys.libraryDependencies

object Settings {
  lazy val commonDependencies     = libraryDependencies ++= Dependencies.commonDeps
  lazy val commonTestDependencies = libraryDependencies ++= Dependencies.commonTestDeps
  lazy val libDependencies        = libraryDependencies ++= Dependencies.libDeps
  lazy val coreDependencies       = libraryDependencies ++= Dependencies.coreDeps
  lazy val adapterDependencies    = libraryDependencies ++= Dependencies.adapterDeps
  lazy val webDependencies        = libraryDependencies ++= Dependencies.webDeps
  lazy val consumerDependencies   = libraryDependencies ++= Dependencies.consumerDeps
  lazy val agentsDependencies     = javaAgents ++= Dependencies.appAgents
}
