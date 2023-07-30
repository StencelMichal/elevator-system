package com.avsystem.elevatorsystem

import java.util.UUID
import scala.math.abs

object Entities {

  case class Elevator(id: ElevatorId, defaultFloor: Int, minFloor: Floor, maxFloor: Floor)

  case class ElevatorStateSnapshot(
      elevatorId: ElevatorId,
      floor: Floor,
      movement: Movement,
      floorsToVisit: Set[Floor]
  )

  case class ElevatorId(id: Int) extends AnyVal

  case class Floor(floorNumber: Int) extends AnyVal {
    def getDirectionTo(nextFloor: Floor): Direction = if (nextFloor.floorNumber > floorNumber) Up else Down
    def distance(other: Floor): Int                 = abs(floorNumber - other.floorNumber)
    def higherFrom(other: Floor): Boolean           = floorNumber > other.floorNumber
    def higherOrEqualFrom(other: Floor): Boolean    = floorNumber >= other.floorNumber
    def lowerFrom(other: Floor): Boolean            = floorNumber < other.floorNumber
    def lowerOrEqualFrom(other: Floor): Boolean     = floorNumber <= other.floorNumber
  }
  object Floor{
    implicit val ordering: Ordering[Floor] = Ordering.fromLessThan((f1, f2) => f1.lowerFrom(f2))
  }

  sealed trait Movement {
    def swap: Movement = this match {
      case GoingUp   => GoingDown
      case GoingDown => GoingUp
      case Inactive  => Inactive
    }
  }
  object Movement {
    def fromDirection(direction: Direction): Movement = direction match {
      case Up   => GoingUp
      case Down => GoingDown
    }
  }
  case object GoingUp   extends Movement
  case object GoingDown extends Movement
  case object Inactive  extends Movement

  sealed trait Direction
  object Direction {
    def fromString(value: String): Direction = {
      List(Up, Down).find(_.toString.toLowerCase() == value.toLowerCase()).get
    }
  }
  case object Up   extends Direction
  case object Down extends Direction
}