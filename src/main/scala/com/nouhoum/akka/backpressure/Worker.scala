package com.nouhoum.akka.backpressure

import akka.actor.{ActorLogging, ActorRef, Actor}
import com.nouhoum.akka.backpressure.Worker.{StartWorking, Task}
import scala.concurrent.Future
import com.nouhoum.akka.backpressure.Master.GetTask
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

class Worker(master: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case StartWorking =>
      log.info(s"Start working")
      master ! GetTask
    case task @ Task(from, size) =>
      log.info(s"Processing task $task")
      fetchData(from, size) onSuccess {
        case data =>  master ! GetTask
      }
  }

  def fetchData(from: Int, size: Int): Future[List[String]] = {
    Future.successful(randoms(3))
  }

  def randoms(n:Int): List[String] =
   n match {
     case 0 => Nil
     case 1 => Random.alphanumeric.toString() :: Nil
     case _ => Random.alphanumeric.toString() :: Nil ++ randoms(n - 1)
   }
}

object Worker {
  case class Task(from: Int, size: Int)
  case object StartWorking
}
