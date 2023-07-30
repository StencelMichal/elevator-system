package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities._

class SimulationEngine (simulationState: SimulationState){

  def fetchSimulationSnapshot(): List[ElevatorStateSnapshot] = simulationState.elevatorStates.map {
    case (id, state) => toElevatorStateSnapshot(id, state)
  }.toList

  def performSimulationStep(): Unit =
    SimulationUpdater.performSimulationStep(simulationState.elevators, simulationState.elevatorStates)

  def assignTaskToElevator(elevatorId: ElevatorId, requestedFloor:Floor): Unit = {
    //TODO validation
    simulationState.elevatorStates(elevatorId).floorsToVisit += requestedFloor
  }

  def updateState(elevatorId: ElevatorId, newFloor: Floor, newFloorsToVisit: Set[Floor]): ElevatorStateSnapshot = {
    val elevatorState = simulationState.elevatorStates(elevatorId)
    elevatorState.floor = newFloor
    elevatorState.floorsToVisit = newFloorsToVisit
    toElevatorStateSnapshot(elevatorId, elevatorState)
  }

  private def toElevatorStateSnapshot(elevatorId: ElevatorId, state: ElevatorState): ElevatorStateSnapshot =
    ElevatorStateSnapshot(
      elevatorId = elevatorId,
      floor = state.floor,
      movement = state.movement,
      floorsToVisit = state.floorsToVisit
    )

}


