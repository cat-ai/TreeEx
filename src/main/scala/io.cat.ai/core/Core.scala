package io.cat.ai.core

import java.io.File

import io.cat.ai.core.graph.Graph
import io.cat.ai.core.file.FileOps._

import scala.annotation.tailrec

object Core {

  private object graph {

    @tailrec
    def build(edges: Stream[File], acc: Stream[File]): Stream[File] = edges match {
      case Stream.Empty => acc

      case x #:: xs if x.isDirectory => build(x.safeListFiles.toStream #::: xs, x #:: acc)

      case x #:: xs => if (x.getName()(0) != '.') build(xs, x #:: acc) else build(xs, acc)
    }

    def build(file: File): Graph[File] = file match {

      case x if x.isFile && x.getName()(0) == '.' => Graph.empty[File]

      case x if x.isFile => Graph.one(x)

      case x if x.isDirectory => Graph(x, x.safeListFiles map build)
    }

    def buildExcluding(file: File, excludeEdge: Stream[String]): Graph[File] = {

      def build(file: File): Graph[File] = file match {

        case x if x.isFile && x.getName()(0) == '.' => Graph.empty[File]

        case x if x.isFile => if (excludeEdge exists(_ matches x.getName)) Graph.empty[File] else Graph.one(x)

        case x if x.isDirectory => Graph(x, x.safeListFiles filterNot { edge => excludeEdge exists(_ matches edge.getName) } map build)
      }

      build(file)
    }
  }

  def graph(path: String, exclude: List[String]): Graph[File] = graph(new File(path), exclude.toStream)

  def graph(path: String): Graph[File] = graph(new File(path))

  def graph(file: File, excludeEdges: Stream[String]): Graph[File] = {

    if (file.isDirectory) Graph(file, file.safeListFiles filterNot(excludeEdges contains _.getName) map(graph.buildExcluding(_, excludeEdges)))

    else throw new IllegalArgumentException("Not directory")
  }

  def graph(file: File): Graph[File] = {

    if (file.isDirectory) Graph(file, file.safeListFiles map graph.build)

    else throw new IllegalArgumentException("Not directory")
  }
}
