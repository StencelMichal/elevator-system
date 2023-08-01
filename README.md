# **Elevator Control System**

## Assumptions:
* Interaction with the system is done through a terminal interface.
* In one simulation step, each elevator moves the distance of one floor.
* Each elevator has a range of movement - a minimum and maximum floor. If no information about the elevator is provided in the configuration, default values for the system will be set.
* Each elevator has a set of floors it should visit.
* The elevator can be in one of three states:
  * Going up
  * Going down
  * Idle
* Elevator movement is periodic in an up-and-down pattern. This means that the elevator will continue to travel in the current direction until it has pending requests on subsequent floors. Only when it reaches the last floor in the current direction will it change direction or become idle.

## Elevator Algorithm:
Elevators are assigned using greedy algorithm which calculates the shortest "distance" to a given floor and the direction of travel at the time of the request.

**Cases**:
* The elevator is inactive or traveling in the same direction as the request but is still before the requested floor.

  Distance is the number of floors between the current position of the elevator and the requested floor.

* The elevator is traveling in the opposite direction:

  Distance is the sum of:
  * The number of floors from the current position to the last floor in the current direction.
  * The number of floors from the last floor to the requested floor.

* The elevator is traveling in the same direction as the request, but it has already passed the requesting floor:

  Distance is the sum of:
  * The number of floors from current position to the last request in the current direction.
  * The number of floors between the extreme floors in the queue.
  * The number of floors from the last floor in the opposite direction to the requesting floor.

## Running the Simulation:
The project is built using SBT. To run, type:

`sbt run`

## Interaction
The system can be controlled by entering commands in the terminal.
List of available commands:
* **pickup:**
  * Description: Calls an elevator to a specific floor with the intention of traveling in a particular direction.
  * Parameters:
    * [1] floor number
    * [2] elevator direction, possible values are `up` or `down`
  * Example: `pickup 3 down`
* **step:**
  * Description: Performs a simulation step.
  * Example: `step`
* **status:**
  * Description: Shows the status of the current simulation.
  * Example: `status`
* **update:**
  * Description: Updates the elevator state of the given ID.
  * Parameters:
    * [1] elevator id
    * [2] the floor to which the elevator state should be changed
    * [3] whitespace-separated array of floor numbers in square brackets - list of floors to which the elevator should go next
  * Example: `update 3 1 [4 5]`
* **help:**
  * Description: Prints help with all possible commands.
  * Example: `help`

## Further System Enhancements
* Better test coverage. Currently, tests verify the correctness of selecting the elevator that should be chosen by the algorithm. Specifically, tests verifying the correctness of responses after user interactions with the system are needed.
* In the current form, the CLI communicates directly with the simulation. It should be separated from the simulation through an API. A suitable place for this could be `CommandLineDispatcher`, which should make requests/delegate request execution to the simulation. This way, we would have the possibility of interacting with the system in other ways: GUI, HTTP, web application, etc. This would also allow us to run two instances of the CLI; one could provide a live view of the current simulation state, while the other operates the simulation.
* Better use of logging. More logging should be added, especially on the simulation side.
* Tracing of requests.
* Creating interactive documentation after exposing all operations on the simulation to the API.
* Better management of simulation state. In the current form, the state is not well protected enough. Potentially, it is possible to eliminate the need to use var's by using Akka and holding the simulation state as the actor state.
* Returning the elevator to the default floor after a specified period of inactivity. Currently, the elevator has a `defaultFloor`, so counting the number of simulation steps without changing the elevator's state should be added. After exceeding a configurable value, the elevator should receive a request to return to the default floor. This feature is complex because we need to introduce task typing for the elevator. This way, we could cancel the return to the default floor when it receives another request.
* Emergency system shutdown. The system should have the ability to shut down in an emergency, which should also be triggered as a graceful shutdown when the system is turned off. All elevators at this point should stop accepting requests and go to the lowest floor in the building (this should be added to the configuration).
* Rethinking system responsibilities. Currently, all requests are ultimately handled by the `SimulationEngine`. This is due to the desire for the least access to mutable simulation state. It is necessary to consider whether this is a good approach.
* Live simulation. A command could be added that, when entered, periodically queries the simulation state and refreshes the terminal.
* Better exception handling. It would be good to catch exceptions caused by the user, such as providing a floor outside the building range, and display appropriate messages in the terminal.
* Improving the elevator selection algorithm. number of stops that the elevator will have to make to reach the requester could be taken into account int in the "distance" metric.
* Periodically recalculating the optimality of assigned tasks to the elevator. Due to the use of the greedy way of choosing an elevator, it may turn out that after several simulation steps, there is an elevator that can serve our request faster.
