package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities.{Elevator, Floor}
import com.avsystem.elevatorsystem.Exceptions.ApplicationException
import com.avsystem.elevatorsystem.util.Logging

private[simulation] object UpdateStateValidations extends Logging {

  private[simulation] case class IllegalStateUpdateException(message: String) extends ApplicationException(message)

  def validateMoveUpward(elevator: Elevator, nextFloor: Floor): Unit = {
    if (elevator.maxFloor < nextFloor) {
      val errorMsg = s"elevator ${elevator.id} could not be moved upward to $nextFloor floor," +
        s" because it reached its max floor ${elevator.maxFloor}"
      log.warn(errorMsg)
      throw IllegalStateUpdateException(errorMsg)
    }
  }

  def validateMoveDownward(elevator: Elevator, nextFloor: Floor): Unit = {
    if (elevator.minFloor > nextFloor) {
      val errorMsg = s"elevator ${elevator.id} could not be moved downward to $nextFloor floor," +
        s" because it reached its min floor ${elevator.minFloor}"
      log.warn(errorMsg)
      throw IllegalStateUpdateException(errorMsg)
    }
  }


}
