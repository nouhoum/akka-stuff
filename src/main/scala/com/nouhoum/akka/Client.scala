package com.nouhoum.akka

import akka.actor.Actor._

object ServiceClient {
  def main(args: Array[String]) {
    run
  }	

  def run = {
    val serviceClient = new ServiceClient
    serviceClient send Store("greeting", "Hello Toto")
    val msg = "Hello there!"
    val result = serviceClient send Retrieve("1234")
    println(":::: Response to message # " + msg + " ===> " + result)

    serviceClient send Retrieve("greeting") match {
      case Response(key, value) =>
        println(":::: Response to message #" + value)
      case msg@_ => println("Service reponse : " + msg)
    }
  }
}

class ServiceClient {
  log.info("Connecting to Text Storage Service...")
  val service = remote.actorFor("service", "localhost", 1234)
  log.info("Connected to the service....")

  def send(msg: Message) = (service !! msg).as[Message].getOrElse(GenericError("Oops!"))
}