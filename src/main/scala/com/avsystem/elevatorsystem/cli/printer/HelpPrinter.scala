package com.avsystem.elevatorsystem.cli.printer

import com.avsystem.elevatorsystem.cli.CommandLineOperations.Command

import scala.io.AnsiColor.{BOLD, GREEN, MAGENTA}

object HelpPrinter extends CommandLinePrinter {

  def printHelp(commands: List[Command]): Unit = {
    val sb = new StringBuilder
    sb.append(line(withModifiers("Commands:", MAGENTA, BOLD)))
    commands.foreach(command => appendCommandHelp(sb, command))
    println(sb.toString())
  }

  private def appendCommandHelp(sb:StringBuilder, command:Command) = {
    sb.append(line(withIndent(1, withBulletPoint(withModifiers(s"${command.name}:", GREEN, BOLD)))))
    sb.append(line(withIndent(4, s"${withModifiers(s"description:", GREEN)} ${command.description}")))
    sb.append(line(withIndent(4, s"${withModifiers(s"example:", GREEN)} `${command.example}`")))
  }

}
