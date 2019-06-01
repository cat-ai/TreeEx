package io.cat.ai.app.config

import com.typesafe.config.{Config, ConfigFactory}

object TreeExConfigFactory {

  private def load = ConfigFactory.load

  def create: TreeExConfig = new TreeExConfig(load)

  def apply(conf: Config): TreeExConfig  = new TreeExConfig(conf)
}
