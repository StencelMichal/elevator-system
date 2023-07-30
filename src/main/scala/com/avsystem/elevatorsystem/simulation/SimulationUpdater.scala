package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities._

object SimulationUpdater {

  private[simulation] def performSimulationStep(
      //TODO validation
      elevators: Map[ElevatorId, Elevator],
      elevatorStates: Map[ElevatorId, ElevatorState]
  ): Unit = {
    setDirectionForInactiveElevatorsWithTask()
    moveElevators()
    checkIfReachedDestination()

    def setDirectionForInactiveElevatorsWithTask(): Unit = {
      elevatorStates.values
        .filter(state => state.movement == Inactive && state.floorsToVisit.nonEmpty)
        .foreach(state => {
          val nextFloor   = state.floorsToVisit.minBy(_.distance(state.floor))
          val direction   = state.floor.getDirectionTo(nextFloor)
          val newMovement = Movement.fromDirection(direction)
          state.movement = newMovement

        })
    }

    def moveElevators(): Unit = {
      elevatorStates.values.foreach(state =>
        state.movement match {
          //TODO validate
          case GoingUp   => state.floor = Floor(state.floor.floorNumber + 1)
          case GoingDown => state.floor = Floor(state.floor.floorNumber - 1)
          case Inactive  =>
        }
      )
    }

    def checkIfReachedDestination(): Unit = {
      elevatorStates.values.filter(_.movement != Inactive).foreach { state =>
        if (state.floorsToVisit.contains(state.floor)) {
          state.floorsToVisit -= state.floor
          state.movement = nextDirectionForElevatorAtDestination(state)
        }
      }
    }

    def nextDirectionForElevatorAtDestination(state: ElevatorState): Movement = state.movement match {
      case GoingUp if !state.floorsToVisit.exists(_.higherFrom(state.floor)) =>
        if (state.floorsToVisit.exists(_.lowerFrom(state.floor))) GoingDown else Inactive
      case GoingDown if !state.floorsToVisit.exists(_.lowerFrom(state.floor)) =>
        if (state.floorsToVisit.exists(_.higherFrom(state.floor))) GoingUp else Inactive
      case other => other
    }
  }
}
