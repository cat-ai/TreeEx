package io.cat.ai.app.view

import io.cat.ai.app.console.colors._

object ViewFactory {

  def createFileGraphView: FileGraphView =
    new FileGraphView(lineColor = White, dirColor = Blue, fileColor = Green, foundColor = Red, markColor = Red)

  def apply(lineColor: ConsoleColor,
            dirColor: ConsoleColor,
            fileColor: ConsoleColor,
            foundColor: ConsoleColor,
            markedColor: ConsoleColor): FileGraphView = new FileGraphView(lineColor, dirColor, fileColor, foundColor, markedColor)
}
