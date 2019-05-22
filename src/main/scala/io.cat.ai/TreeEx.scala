package io.cat.ai

import java.io.File

import io.cat.ai.console.CLI
import io.cat.ai.core.Core
import io.cat.ai.core.graph.Graph
import io.cat.ai.core.view.{FileGraphRenderer, FileGraphView, Renderer}

object TreeEx extends App {

  val params = CLI.handleArgs(args)

  val fileGraphView = FileGraphView()

  implicit val fileGraphRenderer: Renderer[Graph[File]] = new FileGraphRenderer(fileGraphView)

  val graph = Core.graph(params.path)

  Core.render(graph)
}
