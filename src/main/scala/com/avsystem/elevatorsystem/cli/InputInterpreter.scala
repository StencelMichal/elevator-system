package com.avsystem.elevatorsystem.cli

import com.avsystem.elevatorsystem.Entities.{Direction, ElevatorId, Floor}
import com.avsystem.elevatorsystem.cli.CommandLineOperations._
import com.avsystem.elevatorsystem.cli.RequestResponse._
import com.avsystem.elevatorsystem.cli.printer.SystemMessagePrinter
import com.avsystem.elevatorsystem.util.Logging

import scala.io.StdIn.readLine
import scala.util.Try

object InputInterpreter extends Logging {

  private val welcomingMessage = "Type commands to operate elevator system. For a list of all commands, type `help`"
  private val emptyInputRegex  = """\s*""".r

  def interpretInLoop(implicit systemDispatcher: CommandLineDispatcher): Unit = {
    SystemMessagePrinter.printSystemMessage(welcomingMessage)
    while (true) {
      val input = readLine()
      Try(input match {
        case PickupElevatorCommand.regex(floorNumber, direction) =>
          PickupElevatorCommand.performOperation(
            ElevatorPickupRequest(Floor(floorNumber.toInt), Direction.fromString(direction.toLowerCase.capitalize))
          )
        case PerformStepCommand.regex() =>
          PerformStepCommand.performOperation(PerformStepRequest)
        case GetStatusCommand.regex() =>
          GetStatusCommand.performOperation(GetStatusRequest)
        case UpdateStateCommand.regex(elevatorId, newFloor, newFloorsToVisit) =>
          UpdateStateCommand.performOperation(
            UpdateStateRequest(
              ElevatorId(elevatorId.toInt),
              Floor(newFloor.toInt),
              newFloorsToVisit.split("\\s+").map(floorNumber => Floor(floorNumber.toInt)).toSet
            )
          )
        case HelpCommand.regex() =>
          HelpCommand.showHelp()
        case emptyInputRegex() =>
      }).recover { case t: Throwable =>
        onError(t)
      }

    }

  }

  private def onError(t: Throwable): Unit = {
    log.warn(s"Cannot parse input, reason: $t")
    SystemMessagePrinter.printSystemMessage("Unparsable input. For all possible operations type `help`")
  }

}
