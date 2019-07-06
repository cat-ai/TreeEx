package io.cat.ai.app

import java.io.File

import io.cat.ai.app.console.args.TreeExArgs
import io.cat.ai.app.processor.TreeExFileProcessor
import io.cat.ai.app.util.TreeExOptions
import io.cat.ai.core.graph.Graph

trait App[F[_]] extends TreeExApp[F, Seq[String], TreeExArgs, TreeExOptions, Graph[File], TreeExFileProcessor] {

  def parseArgs(args: Seq[String]): F[TreeExArgs]

  def buildOptions(treeExArgs: TreeExArgs): F[TreeExOptions]

  def createGraph(options: TreeExOptions): F[Graph[File]]

  def createProcessor(options: TreeExOptions): F[TreeExFileProcessor]
}

object App {
  def apply[F[_]](implicit app: App[F]): App[F] = app
}
