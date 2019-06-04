package io.cat.ai

import io.cat.ai.app.TreeEx
import io.cat.ai.app.config.TreeExConfigFactory
import io.cat.ai.app.console.CLIFactory

object Main extends App {

  val treeExConfig = TreeExConfigFactory.create

  val cli = CLIFactory(treeExConfig)

  val treeEx = new TreeEx(cli)

  treeEx.run(args)
}
