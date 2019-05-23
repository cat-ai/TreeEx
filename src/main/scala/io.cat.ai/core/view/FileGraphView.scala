package io.cat.ai.core.view

import java.io.File

import io.cat.ai.console._
import io.cat.ai.core.file.FileOps._

case class FileGraphView(lineColor: ConsoleColor,
                         dirColor: ConsoleColor,
                         fileColor: ConsoleColor,
                         foundColor: ConsoleColor,
                         markColor: ConsoleColor) {

  def dir(name: String): String = s"${dirColor.color}$name${dirColor.reset}"

  def file(name: String): String = s"${fileColor.color}$name${fileColor.reset}"

  def found(name: String): String = s"${foundColor.underlined}$name${foundColor.reset}"

  def mark(name: String): String = s"${markColor.color}$name${markColor.reset}"

  def graphValue(elem: File): String = if (elem.isDirectory) dir(elem.getName) else file(elem.getName)

  def edgeView: String = s"${lineColor.color}├── ${lineColor.reset}"

  def childEdgeView: String = s"${lineColor.color}│   ${lineColor.reset}"

  def oneEdgeView: String = s"${lineColor.color}└── ${lineColor.reset}"

  def foundEdge(elem: File): String = found(elem.getName)

  def lastModified(elem: File): String = s"${foundColor.color}${elem.lastModifiedDate}${foundColor.reset}"

  def empty: String = "    "
}

object FileGraphView {

  def apply(lineColor: ConsoleColor,
            dirColor: ConsoleColor,
            fileColor: ConsoleColor,
            foundColor: ConsoleColor,
            markedColor: ConsoleColor): FileGraphView = new FileGraphView(lineColor, dirColor, fileColor, foundColor, markedColor)

  def default: FileGraphView = FileGraphView(lineColor = White, dirColor = Blue, fileColor = Green, foundColor = Red, markedColor = Red)
}