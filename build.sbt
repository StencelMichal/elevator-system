import Dependencies._

ThisBuild / scalaVersion     := "2.13.11"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "av-elevator-system",
    libraryDependencies ++= Seq(
      munit % Test,
      "com.typesafe"   % "config"    % "1.4.2",
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
