package io.cat.ai.app.console

trait TreeExConsole[F[_]] {
  def writeLine(line: String): F[Unit]
}

object TreeExConsole {

  implicit val consoleIo: TreeExConsole[Out] = (line: String) => Out(println(line))

  def writeLn[F[_]](line: String)(implicit console: TreeExConsole[F]): F[Unit] = console.writeLine(line)
}