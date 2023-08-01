package com.avsystem.elevatorsystem.cli.printer

import scala.io.AnsiColor

trait CommandLinePrinter {

  private val separatorWidth = 80
  protected val separator: String = AnsiColor.BOLD + AnsiColor.MAGENTA + "-" * separatorWidth + AnsiColor.RESET

  def withModifiers(str:String, modifiers:String*): String =
    modifiers.reduce[String](_ + _) + str + AnsiColor.RESET

  def unary_+ : String = ""

  def line(str:String): String = str + '\n'

  def withTab(str:String):String = '\t' + str

  def withIndent(indentSize:Int, str:String): String = " " * indentSize + str

  def withBulletPoint(str:String) = s"\u2022 $str"

}
