package io.cat.ai.app

import java.io.File

import io.cat.ai.core.view.FileGraphView

case class TreeExFileProcessor(mode: TreeExMode, view: FileGraphView) {

  def process(file: File): String =

    if (mode.findValues exists(_ matches file.getName))
      processFound(file)
    else if (file.isDirectory)
      processDirectory(file)
    else
      processFile(file)

  private def processDirectory(file: File): String = if (mode.markDirectories) processMarked(file) else view.dir(file.getName)

  private def processFile(file: File): String = if (mode.markFiles) processMarked(file) else view.file(file.getName)

  private def processFound(file: File): String =
    if (mode.markLm) s"${view.foundEdge(file)} : ${view.lastModified(file)}"
    else view.foundEdge(file)

  private def processMarked(file: File): String = view.mark(file.getName)
}
