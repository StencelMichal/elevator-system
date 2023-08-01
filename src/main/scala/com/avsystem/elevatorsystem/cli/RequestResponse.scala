package com.avsystem.elevatorsystem.cli

import com.avsystem.elevatorsystem.Entities.{Direction, ElevatorId, ElevatorStateSnapshot, Floor}

object RequestResponse {

  sealed trait Request
  sealed trait Response

  case class ElevatorPickupRequest(floor: Floor, direction: Direction) extends Request
  case class ElevatorPickupResponse(elevatorId: Option[ElevatorId]) extends Response

  case object PerformStepRequest extends Request
  case object PerformStepResponse extends Response

  case object GetStatusRequest extends Request
  case class GetStatusResponse(elevators:List[ElevatorStateSnapshot]) extends Response

  case class UpdateStateRequest(elevatorId: ElevatorId, newFloor:Floor, newFloorsToVisit:Set[Floor]) extends Request
  case class UpdateStateResponse(elevatorStateSnapshot: ElevatorStateSnapshot) extends Response

}
