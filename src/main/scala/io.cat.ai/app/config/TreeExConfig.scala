package io.cat.ai.app.config

import com.typesafe.config.Config

import io.cat.ai.app.console.args.{CLArg, CLArgValue, CLArgsValue}

import scala.collection.JavaConverters._

final case class TreeExConfig(conf: Config) {

  lazy val appArgs: Vector[CLArg] =
    for {
      appArg <- conf.getObjectList("appArgs").asScala.toVector
      conf <- Vector(appArg.toConfig)
    } yield
      CLArg(conf.getString("description"), conf.getString("arg"))

  lazy val appArgValues: Vector[CLArgsValue] =
    for {
      appArgVals <- conf.getObjectList("appArgValues").asScala.toVector
      conf <- Vector(appArgVals.toConfig)
      vals <- conf.getObjectList("values").asScala.toVector
      valConf <- Vector(vals.toConfig)
    } yield
      CLArgsValue(conf.getString("description"), CLArgValue(valConf.getString("value"), valConf.getString("lit")))

  def argByDescr(descr: String): Option[String] = appArgs find(_.description == descr) map(_.value)

  def argValue(descr: String, value: String): Option[String] =
    appArgValues filter(_.description == descr) map(_.values) find(_.value == value) map(_.lit)

  def getClArgSplitter: String = conf.getString("parser.splitter")
}
