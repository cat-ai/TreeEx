package io.cat.ai.core.view

import java.io.File

import io.cat.ai.core.graph.Graph

// TODO: add file/dir finder, value finder, file/dir marker, file/dir counter
class FileGraphRenderer(view: FileGraphView) extends Renderer[Graph[File]] {

  def renderEdge(prefix: String,
                 edgeView: String,
                 nextOrOneEdgeView: String)(graph: Graph[File]): Unit = {

    Console.println(s"$prefix$edgeView${view.graphValue(graph.value)}")

    if (graph.value.isDirectory) renderGraph(graph, s"$prefix$nextOrOneEdgeView")
  }

  def renderGraph(graph: Graph[File], prefix: String): Unit = {

    for (edge <- graph.edges dropRight 1 if edge.nonEmpty) renderEdge(prefix, view.edgeView, view.childEdgeView)(edge)

    for (oneEdge <- graph.edges.lastOption) renderEdge(prefix, view.oneEdgeView, view.empty)(oneEdge)
  }

  override def render(graph: Graph[File]): Unit = renderGraph(graph, "")
}
