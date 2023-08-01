package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities._

class SimulationEngine (simulationState: SimulationState){

  def fetchSimulationSnapshot: List[ElevatorStateSnapshot] =
    simulationState.elevatorSimulationsById.values.map(_.toElevatorStateSnapshot).toList

  def performSimulationStep(): Unit =
    SimulationUpdater.performSimulationStep(simulationState)

  def assignTaskToElevator(elevatorId: ElevatorId, requestedFloor:Floor): Unit = {
    simulationState.elevatorSimulationsById(elevatorId).state.floorsToVisit += requestedFloor
  }

  def updateState(elevatorId: ElevatorId, newFloor: Floor, newFloorsToVisit: Set[Floor]): ElevatorStateSnapshot = synchronized {
    val elevatorSimulation = simulationState.elevatorSimulationsById(elevatorId)
    elevatorSimulation.state.floor = newFloor
    elevatorSimulation.state.floorsToVisit = newFloorsToVisit
    elevatorSimulation.toElevatorStateSnapshot
  }

}


