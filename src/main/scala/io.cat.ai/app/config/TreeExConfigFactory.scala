package io.cat.ai.app.config

import com.typesafe.config.{Config, ConfigFactory}

object TreeExConfigFactory {

  private def load(resource: String): Config = ConfigFactory.load(resource)

  def create: TreeExConfig = new TreeExConfig(load("application.conf"))

  def apply(conf: Config): TreeExConfig = new TreeExConfig(conf)
}
