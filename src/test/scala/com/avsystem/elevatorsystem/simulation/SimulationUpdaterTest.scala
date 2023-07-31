package com.avsystem.elevatorsystem.simulation

import com.avsystem.elevatorsystem.Entities.{Elevator, ElevatorId, ElevatorStateSnapshot, Floor, GoingUp, Inactive}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class SimulationUpdaterTest extends AnyFlatSpecLike with GivenWhenThen {

  it should "direct elevator to its default floor on move to higher floor then its max" in {
    Given("elevator on its max floor")
    val minFloor = Floor(1)
    val maxFloor = Floor(10)
    val defaultFloor = Floor(3)
    val elevator = Elevator(ElevatorId(1), defaultFloor, minFloor, maxFloor)
    val state = new ElevatorState(maxFloor, Inactive, Set(maxFloor + 1))
    val elevatorSimulation = ElevatorSimulation(elevator, state)
    val simulationState = SimulationState(List(elevatorSimulation.elevator.id -> elevatorSimulation).toMap)

    When("simulation step is being performed")
    SimulationUpdater.performSimulationStep(simulationState)

    Then("elevator should be directed to its default floor")
    state.movement shouldBe Inactive
    state.floor shouldBe maxFloor
    state.floorsToVisit shouldBe Set(defaultFloor)
  }

  it should "direct elevator to its default floor on move to lower floor then its min" in {
    Given("elevator on its max floor")
    val minFloor = Floor(1)
    val maxFloor = Floor(10)
    val defaultFloor = Floor(3)
    val elevator = Elevator(ElevatorId(1), defaultFloor, minFloor, maxFloor)
    val state = new ElevatorState(minFloor, Inactive, Set(minFloor - 1))
    val elevatorSimulation = ElevatorSimulation(elevator, state)
    val simulationState = SimulationState(List(elevatorSimulation.elevator.id -> elevatorSimulation).toMap)

    When("simulation step is being performed")
    SimulationUpdater.performSimulationStep(simulationState)

    Then("elevator should be directed to its default floor")
    state.movement shouldBe Inactive
    state.floor shouldBe minFloor
    state.floorsToVisit shouldBe Set(defaultFloor)
  }

}
