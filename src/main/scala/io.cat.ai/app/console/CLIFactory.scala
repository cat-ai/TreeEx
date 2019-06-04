package io.cat.ai.app.console

import io.cat.ai.app.config.TreeExConfig

object CLIFactory {
  def apply(conf: TreeExConfig): TreeExCLI = TreeExCLI(conf)
}
