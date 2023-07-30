package com.avsystem.elevatorsystem

import com.avsystem.elevatorsystem.cli.{CommandLineDispatcher, InputInterpreter}
import com.avsystem.elevatorsystem.simulation.{SimulationEngine, SimulationInitializer}
import com.avsystem.elevatorsystem.util.Logging

object Main extends Logging {

  def main(args: Array[String]): Unit = {
    log.info("initializing system")
    val configuration = ConfigurationParser.config
    val engine        = SimulationInitializer.createSimulationEngine(configuration.elevatorsConfig)
    val dispatcher    = new CommandLineDispatcher(engine)
    log.info("system initialized")
    InputInterpreter.interpretInLoop(dispatcher)
  }

}
