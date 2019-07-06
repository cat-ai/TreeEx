package io.cat.ai.app

import java.io.File

import io.cat.ai.app.console.args._
import io.cat.ai.app.console.TreeExCLI
import io.cat.ai.app.mode.{DefaultMode, SpecMode}
import io.cat.ai.app.processor.TreeExFileProcessor
import io.cat.ai.app.renderer.FileGraphRenderer
import io.cat.ai.app.util.TreeExOptions
import io.cat.ai.app.view.ViewFactory
import io.cat.ai.core
import io.cat.ai.core.graph.Graph
import io.cat.ai.core.task.Task

import Task._

class Runner(cli: TreeExCLI) {

  private val Default: TreeExMarkArg = TreeExMarkArg.default

  def getAppOptions(args: TreeExArgs): TreeExOptions = args match {
    case TreeExArgs(None, _, _, _) =>
      throw new IllegalArgumentException(s"Missed mandatory program argument ${cli.appArgs.mandatory.mkString}")

    case TreeExArgs(Some(path), Nil, Nil, Default) => TreeExOptions(path, DefaultMode)

    case TreeExArgs(Some(path), Nil, Nil, marker) =>
      TreeExOptions(path, SpecMode(Nil, Nil, marker.markLm, marker.markDir, marker.markFile))

    case TreeExArgs(Some(path), findValues, Nil, marker) =>
      TreeExOptions(path, SpecMode(findValues, Nil, marker.markLm, marker.markDir, marker.markFile))

    case TreeExArgs(Some(path), findValues, exValues, marker) =>
      TreeExOptions(path, SpecMode(findValues, exValues, marker.markLm, marker.markDir, marker.markFile))
  }

  implicit val AppTask: App[Task] = new App[Task] {
    def parseArgs(clArgs: Seq[String]): Task[TreeExArgs] = Task.point(cli parseArgs clArgs)

    def buildOptions(treeExArgs: TreeExArgs): Task[TreeExOptions] = Task.point(getAppOptions(treeExArgs))

    def createGraph(treeExOptions: TreeExOptions): Task[Graph[File]] =
      Task.point(core buildGraph(treeExOptions.path, treeExOptions.mode.excludeValues))

    def createProcessor(options: TreeExOptions): Task[TreeExFileProcessor] =
      Task.point(TreeExFileProcessor(options.mode, ViewFactory.createFileGraphView))
  }

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