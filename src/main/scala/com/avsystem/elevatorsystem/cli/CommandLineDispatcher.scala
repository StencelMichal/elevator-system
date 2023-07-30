package com.avsystem.elevatorsystem.cli

import com.avsystem.elevatorsystem.ElevatorCallAlgorithm
import com.avsystem.elevatorsystem.Entities.{Direction, ElevatorId, Floor}
import com.avsystem.elevatorsystem.Exceptions.ApplicationException
import com.avsystem.elevatorsystem.Execution.executionContext
import com.avsystem.elevatorsystem.cli.RequestResponse._
import com.avsystem.elevatorsystem.simulation.SimulationEngine

import scala.concurrent.Future

class CommandLineDispatcher(val simulationEngine: SimulationEngine) {

  def dispatch(request: Request): Future[Response] = request match {
    case ElevatorPickupRequest(floor, direction)                    => callElevator(floor, direction)
    case PerformStepRequest                                         => performSimulationStep
    case GetStatusRequest                                           => getStatus
    case UpdateStateRequest(elevatorId, newFloor, newFloorsToVisit) => updateState(elevatorId, newFloor, newFloorsToVisit)
    case _                                                          => throw ApplicationException(s"CommandLineDispatcher cannot handle $request")
  }

  private def callElevator(requestedFloor: Floor, requestedDirection: Direction): Future[ElevatorPickupResponse] =
    Future {
      val snapshot   = simulationEngine.fetchSimulationSnapshot()
      val elevatorId = ElevatorCallAlgorithm.findElevatorToCall(requestedFloor, requestedDirection, snapshot)
      simulationEngine.assignTaskToElevator(elevatorId, requestedFloor)
      ElevatorPickupResponse(elevatorId)
    }

  private def performSimulationStep: Future[PerformStepResponse.type] =
    Future {
      simulationEngine.performSimulationStep()
      PerformStepResponse
    }

  private def getStatus: Future[GetStatusResponse] =
    Future {
      val snapshots = simulationEngine.fetchSimulationSnapshot()
      GetStatusResponse(snapshots)
    }

  private def updateState(elevatorId: ElevatorId, newFloor: Floor, newFloorsToVisit: Set[Floor]): Future[UpdateStateResponse] =
    Future {
      val currentStateSnapshot = simulationEngine.updateState(elevatorId, newFloor, newFloorsToVisit)
      UpdateStateResponse(currentStateSnapshot)
    }

}
