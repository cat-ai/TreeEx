package io.cat.ai

import io.cat.ai.app.Runner
import io.cat.ai.app.config.{TreeExConfig, TreeExConfigFactory}
import io.cat.ai.app.console.args.TreeExArgs
import io.cat.ai.app.console.{CLI, TreeExCLI}

object Main extends App {

  val config: TreeExConfig = TreeExConfigFactory.create

  implicit val cli: TreeExCLI[TreeExArgs, Seq[String]] = CLI.create(config)

  val runner: Runner = new Runner

  runner.main(args)
}
