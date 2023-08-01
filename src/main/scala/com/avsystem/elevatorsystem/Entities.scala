package com.avsystem.elevatorsystem

import java.util.UUID
import scala.math.abs

object Entities {

  case class Elevator(id: ElevatorId, defaultFloor: Floor, minFloor: Floor, maxFloor: Floor) {
    def hasFloorInRange(floor: Floor): Boolean = minFloor <= floor && floor <= maxFloor
  }

  case class ElevatorStateSnapshot(
      elevator: Elevator,
      floor: Floor,
      movement: Movement,
      floorsToVisit: Set[Floor]
  ) {

    def lastFloorInCurrentDirection: Option[Floor] = movement match {
      case GoingUp   => floorsToVisit.maxOption
      case GoingDown => floorsToVisit.minOption
      case Idle      => None
    }

    def lastFloorInOppositeDirection: Option[Floor] = movement match {
      case GoingUp   => floorsToVisit.minOption
      case GoingDown => floorsToVisit.maxOption
      case Idle      => None
    }

  }

  case class ElevatorId(id: Int) extends AnyVal

  case class Floor(floorNumber: Int) extends AnyVal {
    def getDirectionTo(nextFloor: Floor): Direction = if (nextFloor.floorNumber > floorNumber) Up else Down
    def distance(other: Floor): Int                 = abs(floorNumber - other.floorNumber)
    def >(other: Floor): Boolean                    = floorNumber > other.floorNumber
    def >=(other: Floor): Boolean                   = floorNumber >= other.floorNumber
    def <(other: Floor): Boolean                    = floorNumber < other.floorNumber
    def <=(other: Floor): Boolean                   = floorNumber <= other.floorNumber
    def +(numberOfFloors: Int): Floor               = Floor(floorNumber + numberOfFloors)
    def -(numberOfFloors: Int): Floor               = Floor(floorNumber - numberOfFloors)
  }
  object Floor {
    implicit val ordering: Ordering[Floor] = Ordering.fromLessThan((f1, f2) => f1.<(f2))
  }

  sealed trait Movement {
  }
  object Movement {
    def fromDirection(direction: Direction): Movement = direction match {
      case Up   => GoingUp
      case Down => GoingDown
    }
  }
  case object GoingUp   extends Movement
  case object GoingDown extends Movement
  case object Idle      extends Movement

  sealed trait Direction
  object Direction {
    def fromString(value: String): Direction = {
      List(Up, Down).find(_.toString.toLowerCase() == value.toLowerCase()).get
    }
  }
  case object Up   extends Direction
  case object Down extends Direction
}
