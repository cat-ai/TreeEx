package io.cat.ai

import java.io.File

import io.cat.ai.app.{TreeEx, TreeExFileProcessor}
import io.cat.ai.core.Core
import io.cat.ai.core.graph.Graph
import io.cat.ai.renderer.{FileGraphRenderer, Renderer}

object Main extends App {

  val (path, treeExMode) = TreeEx.pathAndMode(args)

  val graph = treeExMode match {
    case mode if mode.excludeValues.nonEmpty => Core.graph(path, mode.excludeValues)
    case _ => Core.graph(path)
  }

  val fileGraphView = TreeEx.view

  val fileProcessor = TreeExFileProcessor(treeExMode, fileGraphView)

  implicit val fileGraphRenderer: Renderer[Graph[File]] = new FileGraphRenderer(fileProcessor)

  Core.render(graph)
}
