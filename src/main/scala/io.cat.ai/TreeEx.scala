package io.cat.ai

import java.io.File

import io.cat.ai.app.{FindAndMarkMode, FindMode, TreeExFileProcessor}
import io.cat.ai.console.CLI
import io.cat.ai.core.Core
import io.cat.ai.core.graph.Graph
import io.cat.ai.core.view.FileGraphView
import io.cat.ai.renderer.{FileGraphRenderer, Renderer}

object TreeEx extends App {

  val (path, mode) = CLI.pathAndMode(args)

  val fileGraphView = FileGraphView.default

  val graph = mode match {

    case FindMode(_, Some(exValue), _, _, _) => Core.graph(path, exValue)

    case FindAndMarkMode(_, Some(exValue), _, _, _) => Core.graph(path, exValue)

    case _ =>  Core.graph(path)
  }

  val fileProcessor = TreeExFileProcessor(mode, fileGraphView)

  implicit val fileGraphRenderer: Renderer[Graph[File]] = new FileGraphRenderer(fileProcessor)

  Core.render(graph)
}
