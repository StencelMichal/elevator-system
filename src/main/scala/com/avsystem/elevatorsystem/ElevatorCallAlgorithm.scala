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

  private def distanceToCaller(requestedFloor: Floor, requestedDirection: Direction, elevatorState: ElevatorStateSnapshot): Int = {
    val sameDirection = isSameDirection(requestedDirection, elevatorState.movement)
    val beforeCaller  = isBeforeCaller(sameDirection, requestedDirection, requestedFloor, elevatorState.floor)
    if (elevatorState.movement == Idle || beforeCaller) {
      elevatorState.floor.distance(requestedFloor)
    } else if (!sameDirection)
      distanceForElevatorInOppositeDirection(elevatorState, requestedFloor)
    else
      distanceForElevatorInSameDirectionWhichPassedCaller(elevatorState, requestedFloor)
  }

  private def distanceForElevatorInOppositeDirection(elevatorState: ElevatorStateSnapshot, requestedFloor: Floor) = {
    val lastFloorInDirection = elevatorState.lastFloorInCurrentDirection
    lastFloorInDirection match {
      case Some(lastFloor) =>
        elevatorState.floor.distance(lastFloor) +
          lastFloor.distance(requestedFloor)
      case None =>
        elevatorState.floor.distance(requestedFloor)
    }
  }

  private def distanceForElevatorInSameDirectionWhichPassedCaller(elevatorState: ElevatorStateSnapshot, requestedFloor: Floor) = {
    val lastFloorInDirection = elevatorState.lastFloorInCurrentDirection
    lastFloorInDirection match {
      case Some(lastFloor) =>
        val lastFloorInOppositeDirection = elevatorState.lastFloorInOppositeDirection.get
        elevatorState.floor.distance(lastFloor) +
          lastFloor.distance(lastFloorInOppositeDirection) +
          lastFloorInOppositeDirection.distance(requestedFloor)
      case None =>
        elevatorState.floor.distance(requestedFloor)
    }
  }

  private def isSameDirection(requestedDirection: Direction, elevatorMovement: Movement) =
    (requestedDirection, elevatorMovement) match {
      case (Up, GoingUp)     => true
      case (Down, GoingDown) => true
      case _                 => false
    }

  private def isBeforeCaller(
      isSameDirection: Boolean,
      requestedDirection: Direction,
      requestedFloor: Floor,
      elevatorFloor: Floor
  ): Boolean = {
    isSameDirection && (requestedDirection match {
      case Up   => requestedFloor >= elevatorFloor
      case Down => requestedFloor <= elevatorFloor
    })
  }

}
