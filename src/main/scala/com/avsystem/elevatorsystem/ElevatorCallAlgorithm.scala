package com.avsystem.elevatorsystem

import com.avsystem.elevatorsystem.Entities._

object ElevatorCallAlgorithm {

  def findElevatorToCall(
      requestedFloor: Floor,
      requestedDirection: Direction,
      snapshots: List[ElevatorStateSnapshot]
  ): Option[ElevatorId] = {
    val elevatorsAbleToHandleRequest = snapshots.filter(_.elevator.hasFloorInRange(requestedFloor))
    val optElevatorId                = elevatorsAbleToHandleRequest.minByOption(state => distanceToCaller(requestedFloor, requestedDirection, state))
    optElevatorId.map(_.elevator.id)
  }

  private def distanceToCaller(requestedFloor: Floor, requestedDirection: Direction, elevatorState: ElevatorStateSnapshot): Int =
    elevatorState.movement match {
      case Inactive =>
        elevatorState.floor.distance(requestedFloor)
      case GoingUp if requestedDirection == Up && elevatorState.floor.<=(requestedFloor) =>
        elevatorState.floor.distance(requestedFloor)
      case GoingUp =>
        elevatorState.floorsToVisit.max.distance(requestedFloor)
      case GoingDown if requestedDirection == Down && elevatorState.floor.>=(requestedFloor) =>
        elevatorState.floor.distance(requestedFloor)
      case GoingDown =>
        elevatorState.floorsToVisit.min.distance(requestedFloor)
    }

}
