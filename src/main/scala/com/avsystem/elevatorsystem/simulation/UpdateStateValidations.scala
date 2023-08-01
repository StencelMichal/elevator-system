package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities.{Elevator, Floor}
import com.avsystem.elevatorsystem.Exceptions.{ApplicationException, UserException}
import com.avsystem.elevatorsystem.util.Logging

private[simulation] object UpdateStateValidations extends Logging {

  private[simulation] case class IllegalStateUpdateException(message: String) extends UserException(message)

  def validateMove(elevator: Elevator, floor: Floor): Unit = {
    if (elevator.maxFloor < floor) {
      val errorMsg = s"elevator ${elevator.id} could not be moved upward to $floor floor," +
        s" because it's max floor is ${elevator.maxFloor}"
      log.warn(errorMsg)
      throw IllegalStateUpdateException(errorMsg)
    }
    if (elevator.minFloor > floor) {
      val errorMsg = s"elevator ${elevator.id} could not be moved downward to $floor floor," +
        s" because it's min floor is ${elevator.minFloor}"
      log.warn(errorMsg)
      throw IllegalStateUpdateException(errorMsg)
    }
  }

}
