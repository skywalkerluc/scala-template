// sbt-tpolecat is an SBT plugin for automagically configuring scalac options according to the project Scala version
addSbtPlugin("au.com.onegeek"       %% "sbt-dotenv"           % "2.1.233")
addSbtPlugin("io.spray"              % "sbt-revolver"         % "0.9.1")
addSbtPlugin("com.github.sbt"        % "sbt-native-packager"  % "1.9.16")
addSbtPlugin("com.lightbend.sbt"     % "sbt-javaagent"        % "0.1.6")
addSbtPlugin("org.typelevel"         % "sbt-tpolecat"         % "0.5.0")
addSbtPlugin("ch.epfl.scala"         % "sbt-scalafix"         % "0.12.1")
addSbtPlugin("org.scalameta"         % "sbt-scalafmt"         % "2.5.2")
addSbtPlugin("com.codecommit"        % "sbt-github-packages"  % "0.5.3")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt"           % "7.4.0")
addSbtPlugin("org.scoverage"         % "sbt-scoverage"        % "2.1.0")
addSbtPlugin("net.vonbuchholtz"      % "sbt-dependency-check" % "5.1.0")
addDependencyTreePlugin
