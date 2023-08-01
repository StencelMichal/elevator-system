package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities._

class SimulationEngine (simulationState: SimulationState){

  def fetchSimulationSnapshot(): List[ElevatorStateSnapshot] = simulationState.elevatorSimulationsById.map {
    case (id, elevatorSimulation) => toElevatorStateSnapshot(id, elevatorSimulation.state)
  }.toList

  def performSimulationStep(): Unit =
    SimulationUpdater.performSimulationStep(simulationState)

  def assignTaskToElevator(elevatorId: ElevatorId, requestedFloor:Floor): Unit = {
    //TODO validation
    simulationState.elevatorSimulationsById(elevatorId).state.floorsToVisit += requestedFloor
  }

  def updateState(elevatorId: ElevatorId, newFloor: Floor, newFloorsToVisit: Set[Floor]): ElevatorStateSnapshot = synchronized {
    val elevatorState = simulationState.elevatorSimulationsById(elevatorId).state
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


