package io.cat.ai.core

import java.io.File

import io.cat.ai.core.graph.Graph
import io.cat.ai.core.view.Renderer
import io.cat.ai.core.file.FileOps._

import scala.annotation.tailrec

object Core {

  private object graph {

    @tailrec
    def build(edges: Seq[File], acc: List[File]): Seq[File] = edges match {

      case Nil => acc

      case x::xs if x.isDirectory => build(x.safeListFiles ::: xs, x :: acc)

      case x::xs => if (x.getName()(0) != '.') build(xs, x :: acc) else build(xs, acc)
    }

    def build(file: File): Graph[File] = file match {

      case x if x.isFile && x.getName()(0) == '.' => Graph.empty[File]

      case x if x.isFile => Graph.one(x)

      case x if x.isDirectory => Graph(x, x.safeListFiles map build)
    }
  }

  def graph(path: String): Graph[File] = graph(new File(path))

  def graph(file: File): Graph[File] =
    if (file.isDirectory) Graph(file, file.safeListFiles map graph.build)
    else throw new IllegalArgumentException("Not directory")

  def render[E](graph: Graph[E])
               (implicit renderer: Renderer[Graph[E]]): Unit = renderer.render(graph)
}
