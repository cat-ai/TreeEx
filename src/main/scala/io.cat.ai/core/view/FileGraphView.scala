package io.cat.ai.core.view

import java.io.File

import io.cat.ai.console._
import io.cat.ai.core.file.FileOps._

// TODO: rename?
case class FileGraphView(lineColor: ConsoleColor,
                         dirColor: ConsoleColor,
                         fileColor: ConsoleColor,
                         foundColor: ConsoleColor) {

  private def dir(name: String): String = s"${dirColor.color}$name${dirColor.reset}"

  private def file(name: String): String = s"${fileColor.color}$name${fileColor.reset}"

  private def found(name: String): String = s"${foundColor.color}$name${foundColor.reset}"

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
            foundColor: ConsoleColor): FileGraphView = new FileGraphView(lineColor, dirColor, fileColor, foundColor)

  def apply(): FileGraphView = FileGraphView(lineColor = White, dirColor = Blue, fileColor = Green, foundColor = Red )
}