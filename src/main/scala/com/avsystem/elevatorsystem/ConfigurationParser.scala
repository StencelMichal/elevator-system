package com.avsystem.elevatorsystem

import com.avsystem.elevatorsystem.Entities.Floor
import com.typesafe.config.{Config, ConfigFactory}

import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.{Failure, Success, Try}

object ConfigurationParser {

  lazy val config: AppConfig = Try(parse) match {
    case Failure(exception)     => throw new RuntimeException(s"Configuration file cannot be parsed, reason: ${exception.getMessage}")
    case Success(configuration) => configuration
  }

  case class AppConfig(elevatorsConfig: ElevatorsConfig)

  case class ElevatorsConfig(
      elevatorsAmount: Int,
      defaultSpecification: ElevatorSpecification,
      elevatorsSpecifications: List[ElevatorSpecification]
  )

  case class ElevatorSpecification(defaultFloor: Int, minFloor: Floor, maxFloor: Floor)

  private def parse: AppConfig = {
    val config: Config = ConfigFactory.load()
    val appConfig      = config.getConfig("app")
    AppConfig(toElevatorsConfig(appConfig))
  }

  private def toElevatorsConfig(config: Config): ElevatorsConfig = {
    val elevatorsConfig = config.getConfig("elevators")
    ElevatorsConfig(
      elevatorsAmount = elevatorsConfig.getInt("elevatorsAmount"),
      defaultSpecification = toElevatorSpecification(elevatorsConfig.getConfig("defaultSpecification")),
      elevatorsSpecifications = elevatorsConfig.getConfigList("elevatorsSpecifications").asScala.toList.map(toElevatorSpecification)
    )
  }

  private def toElevatorSpecification(config: Config): ElevatorSpecification = {
    ElevatorSpecification(
      defaultFloor = config.getInt("defaultFloor"),
      minFloor = Floor(config.getInt("minFloor")),
      maxFloor = Floor(config.getInt("maxFloor"))
    )
  }

}
