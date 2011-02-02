package com.nouhoum.akka

import akka.config.Supervision.{ OneForOneStrategy, Permanent }
import akka.actor.{ Actor, ActorRef }
import akka.stm._
import Actor._
import scala.collection.immutable.Map

class Service extends Actor {
  //Retry 3 times when a throwable occurs with a time interval of 1 second 
  self.faultHandler = OneForOneStrategy(List(classOf[Throwable]), 3, 1000)
  var i = 0

  val NumOfWorkers = 2

  val storage = Map[String, String]()
  val worker1 = actorOf(new Worker("Worker1", storage))
  val worker2 = actorOf(new Worker("Worker2", storage))
  val workers = Array(worker1, worker2)
  //private val storage = TransactionalMap[String, String]

  override def preStart = {
    log.info("==== Registering the service ==== ")

    Actor.remote.start("localhost", 1234)
    Actor.remote.register("service", self)

    log.info("Starting and linking workers...")
    self.startLink(worker1)
    self.startLink(worker2)
    log.info("Workers linked.")
  }

  def receive = {
    case msg =>
      log.info("Processing message %s", msg)
      println(msg)
      pickWorker forward msg
  }

  private def pickWorker: ActorRef = {
    i = (i + 1) % NumOfWorkers
    workers(i)
  }

  override def postStop = {
    log.info("== Shutting down the storage service...")
    self.unlink(worker1)
    self.unlink(worker2)
    worker1.stop()
    worker2.stop()
    log.info("== Storage service STOPPED")
  }
}

class Worker(val name: String, var storage: Map[String, String]) extends Actor {
  self.lifeCycle = Permanent

  def receive = {
    case msg@Store(key, value) =>
      log.info("Worker # %s Processing request #  " + msg, name)
      storage += key -> value
    case msg@Retrieve(key) =>
      log.info("Worker # %s Processing request # " + msg, name)
      val result = storage.get(key)
      result match {
        case Some(data) =>
          log.info("Data mapped to key # %s is " + data, key)
          self.reply(Response(key, data))
        case None =>
          self.reply(NotFound(key))
      }
    case msg@_ =>
      log.info("Worker # %s Processing request #  " + msg, name)
      log.info("Oops! Unknown message# %s", msg)
      self.reply(GenericError("Unknown message: " + msg))
  }
}
