package io.cat.ai

import java.io.File

import io.cat.ai.app.console.TreeExCLI
import io.cat.ai.app.console.args.{TreeExArgs, TreeExMarkArg}
import io.cat.ai.app.mode.{DefaultMode, SpecMode}
import io.cat.ai.app.processor.TreeExFileProcessor
import io.cat.ai.app.util.TreeExOptions
import io.cat.ai.app.view.ViewFactory
import io.cat.ai.core.graph.Graph
import io.cat.ai.core.task.Task

package object app {

  object Implicits {

    private val Default: TreeExMarkArg = TreeExMarkArg.default

    implicit def AppTask(implicit cli: TreeExCLI[TreeExArgs, Seq[String]]): App[Task] = new App[Task] {

      private def getAppOptions(args: TreeExArgs): TreeExOptions = args match {
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

      def parseArgs(clArgs: Seq[String]): Task[TreeExArgs] = Task.point(cli parseArgs clArgs)

      def buildOptions(treeExArgs: TreeExArgs): Task[TreeExOptions] = Task.point(getAppOptions(treeExArgs))

      def createGraph(treeExOptions: TreeExOptions): Task[Graph[File]] =
        Task.point(core buildGraph(treeExOptions.path, treeExOptions.mode.excludeValues))

      def createProcessor(options: TreeExOptions): Task[TreeExFileProcessor] =
        Task.point(TreeExFileProcessor(options.mode, ViewFactory.createFileGraphView))
    }
  }
}
