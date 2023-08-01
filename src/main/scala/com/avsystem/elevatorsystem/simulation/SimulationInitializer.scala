package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.ConfigurationParser.{ElevatorSpecification, ElevatorsConfig}
import com.avsystem.elevatorsystem.Entities.{Elevator, ElevatorId, Floor}

import scala.annotation.tailrec

object SimulationInitializer {

  def createSimulationEngine(elevatorsConfig: ElevatorsConfig): SimulationEngine = {
    val elevators = initialize(elevatorsConfig)
    val state     = SimulationState.initialize(elevators)
    new SimulationEngine(state)
  }

  private def initialize(elevatorsConfig: ElevatorsConfig): List[Elevator] = {
    initializeElevators(elevatorsConfig.elevatorsAmount, elevatorsConfig.elevatorsSpecifications, elevatorsConfig.defaultSpecification)
  }

  @tailrec
  private def initializeElevators(
      amount: Int,
      elevatorSpecifications: List[ElevatorSpecification],
      default: ElevatorSpecification,
      createdElevators: List[Elevator] = Nil
  ): List[Elevator] = {
    if (amount > 0) {
      elevatorSpecifications match {
        case spec :: rest =>
          val elevator = createFromSpecification(amount, spec)
          initializeElevators(amount - 1, rest, default, elevator :: createdElevators)
        case Nil =>
          val elevator = createFromSpecification(amount, default)
          initializeElevators(amount - 1, Nil, default, elevator :: createdElevators)
      }
    } else createdElevators
  }

  private def createFromSpecification(id: Int, specification: ElevatorSpecification): Elevator =
    Elevator(
      ElevatorId(id),
      defaultFloor = Floor(specification.defaultFloor),
      minFloor = specification.minFloor,
      maxFloor = specification.maxFloor
    )

}
