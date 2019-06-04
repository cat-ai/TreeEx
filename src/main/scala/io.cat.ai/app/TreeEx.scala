package io.cat.ai.app

import java.io.File

import io.cat.ai.app.console.args._
import io.cat.ai.app.mode.{DefaultMode, SpecMode, TreeExMode}
import io.cat.ai.app.processor.TreeExFileProcessor
import io.cat.ai.app.view.ViewFactory
import io.cat.ai.app.console.TreeExCLI
import io.cat.ai.app.renderer.{FileGraphRenderer, Renderer}
import io.cat.ai.core.Core
import io.cat.ai.core.graph.Graph

class TreeEx (cli: TreeExCLI) {

  def pathAndMode(args: Array[String]): (String, TreeExMode) = cli parseArgs args match {

    case TreeExArgs(None, _, _, _) => throw new IllegalArgumentException(s"Missed mandatory program argument ${cli.appArgs.mandatory.mkString}")

    case TreeExArgs(Some(value), Nil, Nil, TreeExMarker(false, false, false)) => value -> DefaultMode

    case TreeExArgs(Some(value), Nil, Nil, marker) => value -> SpecMode(Nil, Nil, marker.markLm, marker.markDir, marker.markFile)

    case TreeExArgs(Some(value), findValues, Nil, marker) => value -> SpecMode(findValues, Nil, marker.markLm, marker.markDir, marker.markFile)

    case TreeExArgs(Some(value), findValues, exValues, marker) => value -> SpecMode(findValues, exValues, marker.markLm, marker.markDir, marker.markFile)
  }

  def render[E](graph: Graph[E])
               (implicit renderer: Renderer[Graph[E]]): Unit = renderer.render(graph)

  def run(args: Array[String]): Unit = {

    val (path, treeExMode) = pathAndMode(args)

    val graph = treeExMode match {
      case mode if mode.excludeValues.nonEmpty => Core.graph(path, mode.excludeValues)
      case _ => Core.graph(path)
    }

    val graphView = ViewFactory.createFileGraphView

    val processor = TreeExFileProcessor(treeExMode, graphView)

    implicit val fileGraphRenderer: Renderer[Graph[File]] = FileGraphRenderer(processor)

    render(graph)
  }
}
