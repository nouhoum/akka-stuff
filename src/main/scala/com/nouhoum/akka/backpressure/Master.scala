package com.nouhoum.akka.backpressure

import akka.actor.{ActorLogging, Props, Actor}
import com.nouhoum.akka.backpressure.Master.{GetTask, FetchData}
import com.nouhoum.akka.backpressure.Worker.StartWorking

class Master extends Actor with ActorLogging {
  var tasks = Tasks.empty
  val step = 10
  val numberOfWorkers = 2

  def waiting: Receive = {
    case message @ FetchData(from, to) =>
      log.info(s"Received a data fetching request $message")
      tasks = Tasks.from(from, to, step)
      createAndStartWorkers()
      context.become(working)
  }

  def receive = waiting

  def working: Receive = {
    case GetTask =>
      tasks.next.fold({
        log.info("No more tasks... Waiting then ;-)")
        context.become(waiting)
      }) {
        case (task, newTasks) =>
          log.info(s"sending task $task to a worker.")
          tasks = newTasks
          sender ! task
      }
  }

  def createAndStartWorkers(): Unit = {
    println(s"Creating workers....")
    (0 until numberOfWorkers) foreach(_ => context.actorOf(Props(new Worker(self))) ! StartWorking)
  }
}

object Master {
  case class FetchData(from: Int, to: Int)
  case object GetTask
}
