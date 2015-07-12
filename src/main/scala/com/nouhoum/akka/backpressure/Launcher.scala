package com.nouhoum.akka.backpressure

import akka.actor.{Props, ActorSystem}
import com.nouhoum.akka.backpressure.Master.FetchData

object Launcher extends App {
  println("Launching data fetcher....")
  val system = ActorSystem()
  val master = system.actorOf(Props[Master])

  master ! FetchData(from = 0, to = 1000)
}
