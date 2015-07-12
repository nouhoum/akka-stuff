package com.nouhoum.akka.backpressure

import com.nouhoum.akka.backpressure.Worker.Task
import scala.annotation.tailrec

case class Tasks(values: Vector[Task]) {
  def isDone = values.isEmpty

  def next: Option[(Task, Tasks)] =
    if(isDone) None
    else Option((values.head, copy(values = values.drop(1))))

  def next2: (Option[Task], Tasks) =
    if(isDone) (None, Tasks.empty)
    else (values.headOption, copy(values = values.drop(1)))
}

object Tasks {
  val empty = Tasks(Vector.empty[Task])

  def from(from: Int, to: Int, step: Int): Tasks = {
    @tailrec
    def go(acc: List[Task], from: Int): List[Task] = {
      val newFrom = from + step

      if(newFrom >= to) Task(from, to) :: acc
      else go(
        Task(from, newFrom)::acc, newFrom
      )
    }

    Tasks(go(Nil, from).toVector)
  }
}
