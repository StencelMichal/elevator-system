import sbt._

object TestDependencies {

  lazy val munit = "org.scalameta" %% "munit" % "0.7.29" % Test
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.15" % Test

}
