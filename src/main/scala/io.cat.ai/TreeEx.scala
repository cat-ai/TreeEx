package io.cat.ai

import java.io.File

import io.cat.ai.app._
import io.cat.ai.console.CLI
import io.cat.ai.core.Core
import io.cat.ai.core.graph.Graph
import io.cat.ai.core.view.FileGraphView
import io.cat.ai.renderer.{FileGraphRenderer, Renderer}

import scala.language.postfixOps

object TreeEx extends App {

  val (path, treeExMode) = CLI parseArgs args match {

    case TreeExArgs(Some(value), Nil, Nil, TreeExMarker(false, false, false)) => value -> DefaultMode

    case TreeExArgs(Some(value), Nil, Nil, marker) => value -> SpecifiedMode(Nil, Nil, marker.markLm, marker.markDir, marker.markFile)

    case TreeExArgs(Some(value), findValues, Nil, marker) => value -> SpecifiedMode(findValues, Nil, marker.markLm, marker.markDir, marker.markFile)

    case TreeExArgs(Some(value), findValues, exValues, marker) => value -> SpecifiedMode(findValues, exValues, marker.markLm, marker.markDir, marker.markFile)
  }

  val graph = treeExMode match {
    case mode if mode.excludeValues.nonEmpty => Core.graph(path, mode.excludeValues)
    case _ => Core.graph(path)
  }

  val fileGraphView = FileGraphView.default

  val fileProcessor = TreeExFileProcessor(treeExMode, fileGraphView)

  implicit val fileGraphRenderer: Renderer[Graph[File]] = new FileGraphRenderer(fileProcessor)

  Core.render(graph)
}
