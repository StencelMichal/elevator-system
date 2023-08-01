package com.avsystem.elevatorsystem.cli

import com.avsystem.elevatorsystem.Execution.executionContext
import com.avsystem.elevatorsystem.cli.RequestResponse.{UpdateStateResponse, _}
import com.avsystem.elevatorsystem.cli.printer.{HelpPrinter, StatusPrinter, SystemMessagePrinter}

import scala.util.matching.Regex
import scala.util.{Failure, Success}

object CommandLineOperations {

  // matches `pickup 3 down`
  private val pickupElevatorRegex = """\s*(?i)pickup\s+(\d+)\s+(up|down)\s*""".r

  case object PickupElevatorCommand extends Command with CallbackOperation[ElevatorPickupRequest, ElevatorPickupResponse] {
    override val regex: Regex = pickupElevatorRegex
    override val callback: ElevatorPickupResponse => Unit = response => {
      val message = response.elevatorId match {
        case Some(elevatorId) => s"Elevator: ${elevatorId.id} called"
        case None             => "No elevator is able to handle pickup request"
      }
      SystemMessagePrinter.printSystemMessage(message)
    }
    override val name: String        = "pickup"
    override val description: String = "Calls an elevator to a specific floor with the intention of traveling in a particular direction"
    override val example: String     = "pickup 3 down"
  }

  // matches `step`
  private val performStepRegex = """\s*step\s*""".r

  case object PerformStepCommand extends Command with CallbackOperation[PerformStepRequest.type, PerformStepResponse.type] {
    override val regex: Regex                               = performStepRegex
    override val callback: PerformStepResponse.type => Unit = _ => SystemMessagePrinter.printSystemMessage("Simulation step performed")
    override val name: String                               = "step"
    override val description: String                        = "Performs simulation step"
    override val example: String                            = "step"
  }

  // matches `status`
  private val showStatusRegex = """\s*status\s*""".r

  case object GetStatusCommand extends Command with CallbackOperation[GetStatusRequest.type, GetStatusResponse] {
    override val regex: Regex                        = showStatusRegex
    override val callback: GetStatusResponse => Unit = response => StatusPrinter.printSimulationStatus(response.elevators)
    override val name: String                        = "status"
    override val description: String                 = "Show status of current simulation"
    override val example: String                     = "status"
  }

  // matches `update 3 1 [4 5]`
  private val updateStateRegex = """update\s*(\d+)\s+(\d+)\s+\[\s*(-?\d+(?:\s-?\d+)*)\s*]\s*""".r

  case object UpdateStateCommand extends Command with CallbackOperation[UpdateStateRequest, UpdateStateResponse] {
    override val regex: Regex = updateStateRegex
    override val callback: UpdateStateResponse => Unit = response => {
      SystemMessagePrinter.printSystemMessage("Elevator state was changed successfully")
      StatusPrinter.printElevatorStatus(response.elevatorStateSnapshot)
    }
    override val name: String        = "update"
    override val description: String = "Updates elevator state of given id"
    override val example: String     = "update 3 1 [4 5]"
  }

  private val helpCommandRegex = """\s*help\s*""".r

  case object HelpCommand extends Command {
    override val regex: Regex = helpCommandRegex
    def showHelp(): Unit = {
      val commands = List(PickupElevatorCommand, PerformStepCommand, GetStatusCommand, HelpCommand)
      HelpPrinter.printHelp(commands)
    }
    override val name: String        = "help"
    override val description: String = "Prints help with all possible commands"
    override val example: String     = "help"
  }

  sealed trait Command {
    def regex: Regex
    def name: String
    def description: String
    def example: String
  }

  trait CallbackOperation[REQUEST <: Request, RESPONSE <: Response] {
    def callback: RESPONSE => Unit
    def performOperation(request: REQUEST)(implicit dispatcher: CommandLineDispatcher): Unit = {
      val response = dispatcher.dispatch(request)
      response.onComplete {
        case Failure(exception)          => println("EXCEPTION", exception)
        case Success(response: RESPONSE) => callback(response)
      }
    }
  }

}
