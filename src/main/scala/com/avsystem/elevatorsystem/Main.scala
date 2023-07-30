package com.avsystem.elevatorsystem

import com.avsystem.elevatorsystem.cli.{CommandLineDispatcher, InputInterpreter}
import com.avsystem.elevatorsystem.simulation.{SimulationEngine, SimulationInitializer}

object Main {

  def main(args: Array[String]): Unit = {
    val configuration = ConfigurationParser.config
    val engine = SimulationInitializer.createSimulationEngine(configuration.elevatorsConfig)
    val dispatcher = new CommandLineDispatcher(engine)
    InputInterpreter.interpretInLoop(dispatcher)
  }

}
