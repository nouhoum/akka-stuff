package com.nouhoum.akka

abstract class Message

case class Store(key:String, value:String) extends Message
case class Retrieve(key:String) extends Message
case class NotFound(key: String) extends Message
case class Response(key: String, value: String) extends Message
case class GenericError(message: String) extends Message