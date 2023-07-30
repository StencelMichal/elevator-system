package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities.{Elevator, ElevatorId, Floor, Inactive, Movement}

private[simulation] class ElevatorState(
    var floor: Floor,
    var movement: Movement,
    var floorsToVisit: Set[Floor]
)

case class SimulationState(elevators: Map[ElevatorId, Elevator], elevatorStates: Map[ElevatorId, ElevatorState])
object SimulationState {
  private[simulation] def initialize(elevators: List[Elevator]): SimulationState = {
    val elevatorStatesById = elevators.map { elevator =>
      val state = new ElevatorState(floor = elevator.minFloor, movement = Inactive, floorsToVisit = Set.empty)
      elevator.id -> state
    }.toMap
    new SimulationState(elevators.map(elevator => elevator.id -> elevator).toMap, elevatorStates = elevatorStatesById)
  }
}
