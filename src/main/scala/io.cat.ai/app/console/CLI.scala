package io.cat.ai.app.console

import io.cat.ai.app.config.{TreeExConfig, TreeExConfigFactory}

object CLI {

  def apply(config: TreeExConfig): TreeExCLI = TreeExCLI(config)

  def create(): TreeExCLI = TreeExCLI(TreeExConfigFactory.create)

  def create(config: TreeExConfig): TreeExCLI = apply(config)
}
