package com.avsystem.elevatorsystem

import com.avsystem.elevatorsystem.Entities._

import scala.math.abs

object ElevatorCallAlgorithm {

  def findElevatorToCall(requestedFloor:Floor, requestedDirection:Direction, elevatorsStates:List[ElevatorStateSnapshot]): ElevatorId =
    elevatorsStates.minBy(state => distanceToCaller(requestedFloor, requestedDirection, state)).elevatorId

  private def distanceToCaller(requestedFloor: Floor, requestedDirection: Direction, elevatorState: ElevatorStateSnapshot): Int =
    elevatorState.movement match {
      case Inactive =>
        elevatorState.floor.distance(requestedFloor)
      case GoingUp if requestedDirection == Up && elevatorState.floor.lowerOrEqualFrom(requestedFloor) =>
        elevatorState.floor.distance(requestedFloor)
      case GoingUp =>
        elevatorState.floorsToVisit.max.distance(requestedFloor)
      case GoingDown if requestedDirection == Down && elevatorState.floor.higherOrEqualFrom(requestedFloor) =>
        elevatorState.floor.distance(requestedFloor)
      case GoingDown =>
        elevatorState.floorsToVisit.min.distance(requestedFloor)
    }

//  def findElevatorToCall(requestedFloor:Floor, requestedDirection:Direction, elevatorsStates:List[ElevatorStateSnapshot]): ElevatorId = {
//
//    val inactiveElevatorOnRequestedFloor = elevatorsStates.find(state =>
//      state.floor == requestedFloor && state.movement == Inactive
//    )
//
//
//    val closestInactiveElevatorOrGoingSameDirection = {
//      val inactiveElevators = elevatorsStates.filter(_.movement == Inactive)
//      val elevatorsWithSameDirection = elevatorsStates.filter(state => requestedDirection match {
//        case Up => state.movement == GoingUp && state.floor.floorNumber <= requestedFloor.floorNumber
//        case Down => state.movement == GoingDown && state.floor.floorNumber >= requestedFloor.floorNumber
//      }
//      )
//      (inactiveElevators ::: elevatorsWithSameDirection).minByOption(state => {
//        abs(state.floor.floorNumber - requestedFloor.floorNumber)
//      })
//    }
//
//    //TODO winda która będzie najszybciej w stanie obsłużyć, po zrobieniu aktualnych zadań
//    inactiveElevatorOnRequestedFloor.orElse(closestInactiveElevatorOrGoingSameDirection).get.elevatorId
//  }
}
