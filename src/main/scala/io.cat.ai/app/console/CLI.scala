package io.cat.ai.app.console

import io.cat.ai.app.config.TreeExConfig
import io.cat.ai.app.console.args.TreeExArgs

object CLI {
  def create(config: TreeExConfig): TreeExCLI[TreeExArgs, Seq[String]] = new DefaultTreeExCLI(config)
}
