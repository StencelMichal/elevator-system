package com.avsystem.elevatorsystem

object Exceptions {

  case class ApplicationException(message: String) extends Throwable
  case class UserException(message: String)        extends Throwable

}
