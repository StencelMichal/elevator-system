package com.avsystem.elevatorsystem.cli.printer

import com.avsystem.elevatorsystem.Entities.ElevatorStateSnapshot

import scala.io.AnsiColor.{BOLD, GREEN}

object StatusPrinter extends CommandLinePrinter {

  def printSimulationStatus(elevatorSnapshots: List[ElevatorStateSnapshot]): Unit = {
    val sb = new StringBuilder()
    sb.append(separator)
    elevatorSnapshots.foreach{elevator =>
      appendElevatorStatus(sb, elevator)
      sb.append(separator)
    }
    println(sb.toString())
  }

  def printElevatorStatus(elevatorStateSnapshot: ElevatorStateSnapshot): Unit = {
    val sb = new StringBuilder()
    sb.append(separator)
    appendElevatorStatus(sb, elevatorStateSnapshot)
    sb.append(separator)
    println(sb.toString())
  }

  private def appendElevatorStatus(sb: StringBuilder, elevatorSnapshot: ElevatorStateSnapshot): Unit = {
    sb.append('\n')
    appendElement(sb, "elevatorId", elevatorSnapshot.elevatorId.id)
    appendElement(sb, "currentFloor", elevatorSnapshot.floor.floorNumber)
    appendElement(sb, "movingDirection", elevatorSnapshot.movement)
    appendElement(sb, "floorsToVisit", elevatorSnapshot.floorsToVisit.map(_.floorNumber).mkString("[", ",", "]"))
  }

  private def appendElement[T](sb: StringBuilder, elementName: String, element: T): Unit =
    sb.append(line(withTab(withModifiers(elementName, BOLD + GREEN) + s": $element")))

}
