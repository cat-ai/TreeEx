package io.cat.ai

import io.cat.ai.app.Runner
import io.cat.ai.app.config.{TreeExConfig, TreeExConfigFactory}
import io.cat.ai.app.console.{CLI, TreeExCLI}

object Main extends App {

  val config: TreeExConfig = TreeExConfigFactory.create

  val cli: TreeExCLI = CLI.create(config)

  val runner: Runner = new Runner(cli)

  runner.main(args)
}
