package io.cat.ai.app.renderer

import java.io.File

import io.cat.ai.app.console.TreeExConsole
import io.cat.ai.app.mode.{DefaultMode, SpecMode, TreeExMode}
import io.cat.ai.app.processor.TreeExFileProcessor
import io.cat.ai.app.view.FileGraphWalker
import io.cat.ai.core.graph.Graph

import io.cat.ai.app.console.TreeExConsole._

final case class FileGraphRenderer(processor: TreeExFileProcessor) extends Renderer[Graph[File]] {

  private def option(prefix: String, values: List[String]): String = values match {
    case Nil => ""
    case list => s", $prefix: ${list mkString ","}"
  }

  private def result(walker: FileGraphWalker, mode: TreeExMode): String = mode match {

    case DefaultMode => s"${walker.nDirs} directories, ${walker.nFiles} files"

    case SpecMode(find, exclude, true, _, _) =>
      s"${walker.nDirs} marked directories, ${walker.nFiles} files${option("find", find)}${option("exclude", exclude)}"

    case SpecMode(find, exclude, _, true, _) =>
      s"${walker.nDirs} marked directories, ${walker.nFiles} files${option("find", find)}${option("exclude", exclude)}"

    case SpecMode(find, exclude, _, _, true) =>
      s"${walker.nDirs} directories, ${walker.nFiles} marked files${option("find", find)}${option("exclude", exclude)}"

    case SpecMode(find, exclude, _, _, _) =>
      s"${walker.nDirs} directories, ${walker.nFiles} files${option("find", find)}${option("exclude", exclude)}"
  }

  def renderEdge(prefix: String,
                 edgeView: String,
                 nextOrOneEdgeView: String)
                (walker: FileGraphWalker, graph: Graph[File]): FileGraphWalker = {

    TreeExConsole.writeLn(s"$prefix$edgeView${processor.process(graph.value)}")

    graph.value match {
      case file if file.isDirectory => renderGraph(graph, s"$prefix$nextOrOneEdgeView")(walker.copy(nDirs = walker.nDirs + 1))
      case _ => renderGraph(graph, s"$prefix$nextOrOneEdgeView")(walker.copy(nFiles = walker.nFiles + 1))
    }
  }

  def renderGraph(graph: Graph[File],
                  prefix: String = "")(walker: FileGraphWalker): FileGraphWalker = {

    val walkerEx =
      (walker /: (graph.edges filter(_.nonEmpty) dropRight 1))(renderEdge(prefix, processor.view.edgeView, processor.view.childEdgeView)(_, _))

    (walkerEx /: graph.edges.lastOption.filter(_.nonEmpty))(renderEdge(prefix, processor.view.oneEdgeView, processor.view.empty)(_, _))
  }

  override def render(graph: Graph[File]): Unit = {
    TreeExConsole.writeLn(graph.value.getName)

    val walker = renderGraph(graph)(FileGraphWalker())

    TreeExConsole.writeLn(result(walker, processor.mode))
  }
}