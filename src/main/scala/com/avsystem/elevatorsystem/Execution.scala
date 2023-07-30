package com.avsystem.elevatorsystem

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutor

object Execution {

  implicit val executionContext: ExecutionContextExecutor = {
    val numberOfThreads = Runtime.getRuntime.availableProcessors()
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(numberOfThreads))
  }


}
