package io.cat.ai.app

import java.io.File

import io.cat.ai.app.console.args._
import io.cat.ai.app.console.TreeExCLI
import io.cat.ai.app.processor.TreeExFileProcessor
import io.cat.ai.app.renderer.FileGraphRenderer
import io.cat.ai.app.util.TreeExOptions
import io.cat.ai.core.graph.Graph
import io.cat.ai.core.task.Task

import Task._

import scala.language.higherKinds

class Runner(implicit val cli: TreeExCLI[TreeExArgs, Seq[String]]) {

  import io.cat.ai.app.Implicits._

  def parseAppArgs[F[_]: App](args: Seq[String]): F[TreeExArgs] = App[F].parseArgs(args)

  def buildAppOptions[F[_]: App](treeExArgs: TreeExArgs): F[TreeExOptions] = App[F].buildOptions(treeExArgs)

  def createAppFileGraph[F[_]: App](options: TreeExOptions): F[Graph[File]] = App[F].createGraph(options)

  def createAppFileProcessor[F[_]: App](options: TreeExOptions): F[TreeExFileProcessor] = App[F].createProcessor(options)

  def render(graph: Graph[File], processor: TreeExFileProcessor): Unit = FileGraphRenderer(processor).render(graph)

  def appTask(args: Seq[String]): Task[Unit] =
    for {
      appArgs     <- parseAppArgs(args)
      appOptions  <- buildAppOptions(appArgs)
      processor   <- createAppFileProcessor(appOptions)
      graph       <- createAppFileGraph(appOptions)
    } yield {
      render(graph, processor)
    }

  def main(args: Seq[String]): Unit = appTask(args).runSync
}