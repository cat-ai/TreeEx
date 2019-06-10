package io.cat.ai.app.processor

import java.io.File

import io.cat.ai.app.mode.TreeExMode
import io.cat.ai.app.view.FileGraphView

final case class TreeExFileProcessor(mode: TreeExMode, view: FileGraphView) {

  def process(file: File): String = file match {
    case x if mode.findValues exists(_ matches x.getName) => processFound(x)

    case x if x.isDirectory =>  processDirectory(x)

    case x => processFile(x)
  }

  private def processDirectory(file: File): String = if (mode.markDirectories) processMarked(file) else view.dir(file.getName)

  private def processFile(file: File): String = if (mode.markFiles) processMarked(file) else view.file(file.getName)

  private def processFound(file: File): String =
    if (mode.markLm) s"${view.foundEdge(file)} : ${view.lastModified(file)}"
    else view.foundEdge(file)

  private def processMarked(file: File): String = view.mark(file.getName)
}
