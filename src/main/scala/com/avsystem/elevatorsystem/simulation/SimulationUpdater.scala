package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities._
import com.avsystem.elevatorsystem.Exceptions.ApplicationException
import com.avsystem.elevatorsystem.simulation.UpdateStateValidations.{validateMoveDownward, validateMoveUpward}
import com.avsystem.elevatorsystem.util.Logging

import scala.util.Try

object SimulationUpdater extends Logging {

  private[simulation] def performSimulationStep(simulationState: SimulationState): Unit =
    simulationState.elevatorSimulationsById.values.foreach { elevatorSimulation =>
      Try(synchronized {
        setDirectionIfInactiveWithTask(elevatorSimulation)
        moveElevator(elevatorSimulation)
        checkIfReachedDestination(elevatorSimulation.state)
      }).recover { case _: ApplicationException =>
        directElevatorToDefaultFloor(elevatorSimulation)
      }
    }

  private def setDirectionIfInactiveWithTask(elevatorSimulation: ElevatorSimulation): Unit = {
    val state = elevatorSimulation.state
    if (state.movement == Inactive && state.floorsToVisit.nonEmpty) {
      val nextFloor   = state.floorsToVisit.minBy(_.distance(state.floor))
      val direction   = state.floor.getDirectionTo(nextFloor)
      val newMovement = Movement.fromDirection(direction)
      state.movement = newMovement
    }
  }

  private def moveElevator(simulation: ElevatorSimulation): Unit = {
    val ElevatorSimulation(elevator, state) = simulation
    state.movement match {
      case GoingUp =>
        val nextFloor = state.floor + 1
        validateMoveUpward(elevator, nextFloor)
        state.floor = nextFloor
      case GoingDown =>
        val nextFloor = state.floor - 1
        validateMoveDownward(elevator, nextFloor)
        state.floor = nextFloor
      case Inactive =>
    }
  }

  private def checkIfReachedDestination(state: ElevatorState): Unit = {
    if (state.movement != Inactive && state.floorsToVisit.contains(state.floor)) {
      state.floorsToVisit -= state.floor
      state.movement = nextDirectionForElevatorAtDestination(state)
    }
  }

  private def nextDirectionForElevatorAtDestination(state: ElevatorState): Movement = state.movement match {
    case GoingUp if !state.floorsToVisit.exists(_.>(state.floor)) =>
      if (state.floorsToVisit.exists(_.<(state.floor))) GoingDown else Inactive
    case GoingDown if !state.floorsToVisit.exists(_.<(state.floor)) =>
      if (state.floorsToVisit.exists(_.>(state.floor))) GoingUp else Inactive
    case other => other
  }

  private def directElevatorToDefaultFloor(elevatorSimulation: ElevatorSimulation): Unit = {
    log.info(s"Elevator ${elevatorSimulation.elevator.id} was reset due to error in its state update")
    elevatorSimulation.state.movement = Inactive
    elevatorSimulation.state.floorsToVisit = Set(elevatorSimulation.elevator.defaultFloor)
  }

}
