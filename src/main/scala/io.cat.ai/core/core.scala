package io.cat.ai

import java.io.File

import io.cat.ai.core.graph.Graph
import io.cat.ai.core.file.FileOps._

import scala.annotation.tailrec

package object core {

  object GraphBuilder {

    @tailrec
    def build(edges: Seq[File], acc: Seq[File]): Seq[File] = edges match {
      case Nil => acc

      case x +: xs if x.isDirectory => build(x.safeListFiles.toStream ++: xs, x +: acc)

      case x +: xs => if (x.getName()(0) != '.') build(xs, x +: acc) else build(xs, acc)
    }

    def build(file: File): Graph[File] = file match {
      case x if x.isFile && x.getName()(0) == '.' => Graph.empty[File]

      case x if x.isFile => Graph.one(x)

      case x if x.isDirectory => Graph(x, x.safeListFiles map build)
    }

    def buildExcluding(file: File, excludeEdge: Seq[String]): Graph[File] = {

      def build(file: File): Graph[File] = file match {
        case x if x.isFile && x.getName()(0) == '.' => Graph.empty[File]

        case x if x.isFile => if (excludeEdge exists(_ matches x.getName)) Graph.empty[File] else Graph.one(x)

        case x if x.isDirectory => Graph(x, x.safeListFiles filterNot(edge => excludeEdge exists(_ matches edge.getName)) map build)
      }

      build(file)
    }
  }

  def buildGraph(path: String, exclude: Seq[String] = Nil): Graph[File] = buildGraph(new File(path), exclude.toStream)

  def buildGraph(file: File, excludeEdges: Seq[String]): Graph[File] = file match {
    case x if x.isDirectory && excludeEdges.nonEmpty =>
      Graph(x, x.safeListFiles filterNot(excludeEdges contains _.getName) map(GraphBuilder.buildExcluding(_, excludeEdges)))

    case x if x.isDirectory => Graph(file, file.safeListFiles map GraphBuilder.build)

    case x => throw new IllegalArgumentException(s"$x is not a directory")
  }
}
