package io.cat.ai

import java.io.File

import io.cat.ai.app.TreeExFileProcessor
import io.cat.ai.console.CLI
import io.cat.ai.core.Core
import io.cat.ai.core.graph.Graph
import io.cat.ai.core.view.FileGraphView
import io.cat.ai.renderer.{FileGraphRenderer, Renderer}

object TreeEx extends App {

  val (path, mode) = CLI.pathAndMode(args)

  val fileGraphView = FileGraphView.default

  val settings = TreeExFileProcessor(path, mode, fileGraphView)

  implicit val fileGraphRenderer: Renderer[Graph[File]] = new FileGraphRenderer(settings)

  val graph = Core.graph(settings.path)

  Core.render(graph)
}
