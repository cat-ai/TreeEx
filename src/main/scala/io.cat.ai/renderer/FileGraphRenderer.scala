package io.cat.ai.renderer

import java.io.File

import io.cat.ai.app.TreeExFileProcessor
import io.cat.ai.core.graph.Graph

class FileGraphRenderer(processor: TreeExFileProcessor) extends Renderer[Graph[File]] {

  def renderEdge(prefix: String,
                 edgeView: String,
                 nextOrOneEdgeView: String)(graph: Graph[File]): Unit = {

    Console.println(s"$prefix$edgeView${processor.process(graph.value)}")

    if (graph.value.isDirectory) renderGraph(graph, s"$prefix$nextOrOneEdgeView")
  }

  def renderGraph(graph: Graph[File], prefix: String = ""): Unit = {

    for (edge <- graph.edges dropRight 1 if edge.nonEmpty)
      renderEdge(prefix, processor.view.edgeView, processor.view.childEdgeView)(edge)

    for (oneEdge <- graph.edges.lastOption if oneEdge.nonEmpty)
      renderEdge(prefix, processor.view.oneEdgeView, processor.view.empty)(oneEdge)
  }

  override def render(graph: Graph[File]): Unit = renderGraph(graph)
}
