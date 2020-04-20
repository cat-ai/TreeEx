package io.cat.ai.app.console

import io.cat.ai.app.config.TreeExConfig

import scala.language.higherKinds

trait TreeExCLI[+A, C] {

  def conf: TreeExConfig

  def parseArgs(args: C): A

  object appArgs {
    val path: Option[String]    = conf.argByDescr("path")
    val find: Option[String]    = conf.argByDescr("find")
    val exclude: Option[String] = conf.argByDescr("exclude")
    val mark: Option[String]    = conf.argByDescr("mark")

    val mandatory: Option[String] = path
  }
}