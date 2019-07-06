package io.cat.ai.core.task

final case class Task[+A](execute: () => A) {

  def map[B](f: A => B): Task[B] = Task.point(f(execute()))

  def flatMap[B](f: A => Task[B]): Task[B] = Task.point(f(execute()).execute())
}

object Task {

  def point[A](a: => A): Task[A] = Task(() => a)

  implicit class TaskSyncExecutor[A](task: Task[A]) {
    def runSync: A = task.execute()
  }
}