package com.avsystem.elevatorsystem

object Exceptions {

  class ApplicationException(message: String) extends Throwable(message)
  class UserException(message: String)        extends Throwable(message)

}
