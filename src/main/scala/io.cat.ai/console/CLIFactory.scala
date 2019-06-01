package io.cat.ai.console

import io.cat.ai.app.config.TreeExConfig

object CLIFactory {
  def apply(conf: TreeExConfig): CLI = new CLI(conf)
}
