package io.cat.ai.app.renderer

import java.io.File

import io.cat.ai.app.mode.{DefaultMode, SpecMode, TreeExMode}
import io.cat.ai.app.processor.TreeExFileProcessor
import io.cat.ai.app.console.AppConsole
import io.cat.ai.core.graph.Graph
import io.cat.ai.app.console.AppConsole._

import FileGraphRenderer._

final case class FileGraphRenderer(processor: TreeExFileProcessor) extends Renderer[Graph[File]] {

  def renderEdge(prefix: String,
                 edgeView: String,
                 nextOrOneEdgeView: String)(walker: FileGraphWalker,
                                            graph: Graph[File]): FileGraphWalker = {
    AppConsole.putStrLn(s"$prefix$edgeView${processor.process(graph.value)}").effect

    graph.value match {
      case file if file.isDirectory => renderGraph(graph,s"$prefix$nextOrOneEdgeView")(walker.copy(nDirs = walker.nDirs + 1))
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
    AppConsole.putStrLn(graph.value.getName).effect

    val walker = renderGraph(graph)(FileGraphWalker())

    AppConsole.putStrLn(result(walker, processor.mode)).effect
  }
}

object FileGraphRenderer {

  private def helper(prefix: String, values: Seq[String]): String = values match {
    case Nil => ""
    case list => s", $prefix [${list mkString ", "}]"
  }

  private def result(walker: FileGraphWalker, mode: TreeExMode): String = mode match {
    case DefaultMode => s"${walker.nDirs} directories, ${walker.nFiles} files"

    case SpecMode(find, exclude, true, _, _) =>
      s"${walker.nDirs} directories, ${walker.nFiles} files${helper("find", find)}${helper("exclude", exclude)}"

    case SpecMode(find, exclude, _, true, _) =>
      s"${walker.nDirs} marked directories, ${walker.nFiles} files${helper("find", find)}${helper("exclude", exclude)}"

    case SpecMode(find, exclude, _, _, true) =>
      s"${walker.nDirs} directories, ${walker.nFiles} marked files${helper("find", find)}${helper("exclude", exclude)}"

    case SpecMode(find, exclude, _, _, _) =>
      s"${walker.nDirs} directories, ${walker.nFiles} files${helper("find", find)}${helper("exclude", exclude)}"
  }
}