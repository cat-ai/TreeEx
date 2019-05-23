package io.cat.ai.app

import java.io.File

import io.cat.ai.core.view.FileGraphView

case class TreeExFileProcessor(path: String, mode: TreeExMode, view: FileGraphView) {

  def process(file: File): String =

    if (mode.value matches file.getName) handleFound(file)

    else if (file.isDirectory) handleDirectory(file)

    else handleFile(file)

  private def handleDirectory(file: File): String = if (mode.markDirectories) handleMarked(file) else view.dir(file.getName)

  private def handleFile(file: File): String = if (mode.markFiles) handleMarked(file) else view.file(file.getName)

  private def handleFound(file: File): String =
    if (mode.markLm) s"${view.foundEdge(file)} : ${view.lastModified(file)}"
    else view.foundEdge(file)

  private def handleMarked(file: File): String = view.mark(file.getName)
}
