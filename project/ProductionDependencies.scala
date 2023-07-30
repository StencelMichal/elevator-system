import sbt._

object ProductionDependencies {

    lazy val config  = "com.typesafe"   % "config"          % "1.4.2"
    lazy val logback = "ch.qos.logback" % "logback-classic" % "1.4.7"

}
