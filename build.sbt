ThisBuild / scalaVersion := "2.13.11"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "av-elevator-system",
    libraryDependencies ++=
      Seq(
        ProductionDependencies.config,

        TestDependencies.munit,
        TestDependencies.scalatest
      )
  )
