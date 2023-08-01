package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities.{Elevator, ElevatorId, Floor}
import com.avsystem.elevatorsystem.simulation.UpdateStateValidations.IllegalStateUpdateException
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers.{an, thrownBy}

class UpdateStateValidationsTest extends AnyFlatSpecLike {

  it should "fail validation of elevator move to higher floor then its max floor" in {
    val maxFloor     = Floor(10)
    val nextFloor    = Floor(11)
    val defaultFloor = Floor(3)
    val minFloor     = Floor(1)
    val elevator     = Elevator(ElevatorId(1), defaultFloor, minFloor, maxFloor)
    an[IllegalStateUpdateException] shouldBe thrownBy {
      UpdateStateValidations.validateMove(elevator, nextFloor)
    }
  }

  it should "fail validation of elevator move to lower floor then its min floor" in {
    val maxFloor = Floor(10)
    val defaultFloor = Floor(3)
    val minFloor = Floor(1)
    val nextFloor = Floor(0)
    val elevator = Elevator(ElevatorId(1), defaultFloor, minFloor, maxFloor)
    an[IllegalStateUpdateException] shouldBe thrownBy {
      UpdateStateValidations.validateMove(elevator, nextFloor)
    }
  }

}
