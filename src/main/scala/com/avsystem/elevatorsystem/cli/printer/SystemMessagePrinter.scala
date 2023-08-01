package com.avsystem.elevatorsystem.cli.printer

import scala.Console.{BOLD, MAGENTA}

object SystemMessagePrinter extends CommandLinePrinter {

  private val systemMessagePrompt = withModifiers("[System information] ", MAGENTA, BOLD)

  def printSystemMessage(msg: String): Unit = {
    val message = line(systemMessagePrompt + msg)
    print(message)
  }

}
