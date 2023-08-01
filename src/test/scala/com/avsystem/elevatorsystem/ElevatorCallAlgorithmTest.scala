package com.avsystem.elevatorsystem

import com.avsystem.elevatorsystem.Entities._
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class ElevatorCallAlgorithmTest extends AnyFlatSpecLike with GivenWhenThen with Matchers {

  private val elevator1 = Elevator(ElevatorId(1), Floor(1), Floor(0), Floor(10))
  private val elevator2 = Elevator(ElevatorId(2), Floor(2), Floor(0), Floor(10))
  private val elevator3 = Elevator(ElevatorId(3), Floor(3), Floor(0), Floor(10))

  it should "pick inactive elevator at requested floor" in {
    Given("inactive elevator on the same floor as requested one")
    val elevatorState1 = ElevatorStateSnapshot(elevator1, Floor(1), Inactive, Set.empty)
    val elevatorState2 = ElevatorStateSnapshot(elevator2, Floor(2), Inactive, Set.empty)
    val elevatorState3 = ElevatorStateSnapshot(elevator3, Floor(3), Inactive, Set.empty)

    When("algorithm calculates elevator to call")
    val direction = Up
    val requestedFloor = Floor(2)
    val elevatorStates = List(elevatorState1, elevatorState2, elevatorState3)
    val pickedElevatorId = ElevatorCallAlgorithm.findElevatorToCall(requestedFloor, direction, elevatorStates)

    Then("it should pick elevator from the requested floor")
    pickedElevatorId shouldBe elevatorStates.find(_.floor == requestedFloor).map(_.elevator.id)
  }

  it should "pick closest inactive elevator" in {
    Given("multiple inactive elevators")
    val elevatorState1 = ElevatorStateSnapshot(elevator1, Floor(1), Inactive, Set.empty)
    val elevatorState2 = ElevatorStateSnapshot(elevator2, Floor(4), Inactive, Set.empty)
    val elevatorState3 = ElevatorStateSnapshot(elevator3, Floor(5), Inactive, Set.empty)

    When("algorithm calculates elevator to call")
    val direction = Up
    val requestedFloor = Floor(3)
    val elevatorStates = List(elevatorState1, elevatorState2, elevatorState3)
    val pickedElevatorId = ElevatorCallAlgorithm.findElevatorToCall(requestedFloor, direction, elevatorStates)

    Then("it should pick elevator closest to the requested floor")
    pickedElevatorId shouldBe defined
    pickedElevatorId.get shouldBe elevatorState2.elevator.id
  }

  it should "pick closest elevator going in same direction" in {
    Given("multiple elevators going in the same direction")
    val elevatorState1 = ElevatorStateSnapshot(elevator1, Floor(3), GoingUp, Set(Floor(10)))
    val elevatorState2 = ElevatorStateSnapshot(elevator2, Floor(4), GoingDown, Set(Floor(0)))
    val elevatorState3 = ElevatorStateSnapshot(elevator3, Floor(5), GoingDown, Set(Floor(1)))

    When("algorithm calculates elevator to call")
    val direction = Down
    val requestedFloor = Floor(3)
    val elevatorStates = List(elevatorState1, elevatorState2, elevatorState3)
    val pickedElevatorId = ElevatorCallAlgorithm.findElevatorToCall(requestedFloor, direction, elevatorStates)

    Then("it should pick elevator going the same direction closest to the requested floor")
    pickedElevatorId shouldBe defined
    pickedElevatorId.get shouldBe elevatorState2.elevator.id
  }

  it should "pick elevator with closes floor after direction change" in {
    Given("multiple elevators going in the same direction")
    val elevatorState1 = ElevatorStateSnapshot(elevator1, Floor(3), GoingDown, Set(Floor(1)))
    val elevatorState2 = ElevatorStateSnapshot(elevator2, Floor(4), GoingDown, Set(Floor(3)))
    val elevatorState3 = ElevatorStateSnapshot(elevator3, Floor(5), GoingDown, Set(Floor(2)))

    When("algorithm calculates elevator to call")
    val direction = Up
    val requestedFloor = Floor(5)
    val elevatorStates = List(elevatorState1, elevatorState2, elevatorState3)
    val pickedElevatorId = ElevatorCallAlgorithm.findElevatorToCall(requestedFloor, direction, elevatorStates)

    Then("it should pick elevator with shortest path considering path required to turn back")
    pickedElevatorId shouldBe defined
    pickedElevatorId.get shouldBe elevatorState2.elevator.id
  }

  it should "not pick elevator when requested floor is not in its range" in {
    Given("elevator elevator with min and max floor")
    val elevatorSnapshot = ElevatorStateSnapshot(elevator1, Floor(3), Inactive, Set.empty)

    When("algorithm calculates elevator to call")
    val elevatorStates = List(elevatorSnapshot)
    val pickedElevatorId1 = ElevatorCallAlgorithm.findElevatorToCall(elevator1.maxFloor + 1, Up, elevatorStates)
    val pickedElevatorId2 = ElevatorCallAlgorithm.findElevatorToCall(elevator1.minFloor - 1, Down, elevatorStates)

    Then("no elevator should be returned")
    pickedElevatorId1 shouldBe empty
    pickedElevatorId2 shouldBe empty

  }

}
