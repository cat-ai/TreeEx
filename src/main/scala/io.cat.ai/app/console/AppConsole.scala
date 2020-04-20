package io.cat.ai.app.console

import io.cat.ai.core.task.Task

import scala.io.StdIn
import scala.language.higherKinds

trait AppConsole[F[_]] {
  def putStrLn(line: String): F[Unit]

  def readLine: F[String]
}

object AppConsole {

  type IO[A] = Task[A]

  implicit class IOEffect[A](ioTask: IO[A]) {
    def effect: A = ioTask.execute()
  }

  implicit val AppConsoleIO: AppConsole[IO] = new AppConsole[IO] {
    override def putStrLn(line: String): IO[Unit] = Task.point(scala.Console.println(line))
    override def readLine: IO[String] = Task.point(StdIn.readLine)
  }

  def apply[F[_]](implicit console: AppConsole[F]): AppConsole[F] = console

  def putStrLn[F[_]: AppConsole](line: String): F[Unit] = AppConsole[F].putStrLn(line)

  def readLine[F[_]: AppConsole]: F[String] = AppConsole[F].readLine
}