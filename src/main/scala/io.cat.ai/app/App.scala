package io.cat.ai.app

import java.io.File

import io.cat.ai.app.console.args.TreeExArgs
import io.cat.ai.app.processor.{Processor, TreeExFileProcessor}
import io.cat.ai.app.util.TreeExOptions
import io.cat.ai.core.graph.Graph

trait App[F[_]] extends TreeExApp[F, Seq[String], TreeExArgs, TreeExOptions, Graph[File], Processor[File, String]] {

  def parseArgs(args: Seq[String]): F[TreeExArgs]

  def buildOptions(treeExArgs: TreeExArgs): F[TreeExOptions]

  def createGraph(treeExPathMode: TreeExOptions): F[Graph[File]]

  def createProcessor(treeExMode: TreeExOptions): F[TreeExFileProcessor]
}

object App {
  def apply[F[_]](implicit app: App[F]): App[F] = app
}
