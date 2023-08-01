package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities.{Elevator, ElevatorId, ElevatorStateSnapshot, Floor, Inactive, Movement}

private[simulation] case class ElevatorSimulation(elevator: Elevator, state: ElevatorState) {
  def toElevatorStateSnapshot: ElevatorStateSnapshot =
    ElevatorStateSnapshot(
      elevator = elevator,
      floor = state.floor,
      movement = state.movement,
      floorsToVisit = state.floorsToVisit
    )
}

private[simulation] class ElevatorState(
    var floor: Floor,
    var movement: Movement,
    var floorsToVisit: Set[Floor]
)

case class SimulationState(elevatorSimulationsById: Map[ElevatorId, ElevatorSimulation])
object SimulationState {
  private[simulation] def initialize(elevators: List[Elevator]): SimulationState = {
    val elevatorSimulationsById = elevators.map { elevator =>
      val state = new ElevatorState(floor = elevator.minFloor, movement = Inactive, floorsToVisit = Set.empty)
      elevator.id -> ElevatorSimulation(elevator, state)
    }.toMap
    new SimulationState(elevatorSimulationsById)
  }
}
