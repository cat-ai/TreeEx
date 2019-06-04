package io.cat.ai.app.console

import io.cat.ai.app.config.TreeExConfig
import io.cat.ai.app.console.args.{TreeExArgs, TreeExMarker}

import scala.annotation.tailrec
import scala.language.postfixOps

final case class TreeExCLI(conf: TreeExConfig) {

  object appArgs {
    val path: Option[String] = conf.argByDescr("path")
    val find: Option[String] = conf.argByDescr("find")
    val exclude: Option[String] = conf.argByDescr("exclude")
    val mark: Option[String] = conf.argByDescr("mark")

    val mandatory: Option[String] = path
  }

  object markArg {
    val markDir: Option[String] = conf.argValue("mark", "markDir")
    val markFile: Option[String] = conf.argValue("mark", "markFile")
    val lastModified: Option[String] = conf.argValue("mark", "markLm")
  }

  val isValueIncorrect: String => Boolean = _ startsWith "-"

  val splitToList: (String, String) => List[String] = _ split _ toList

  val split: String => List[String] = splitToList(_, conf.getClArgSplitter)

  def markerFromArg(args: String): TreeExMarker = {

    @tailrec
    def recMarkArgParser(markArgs: List[String], marker: TreeExMarker): TreeExMarker = markArgs match {
      case Nil => marker
      case d :: tail if markArg.markDir contains d => recMarkArgParser(tail, marker.copy(markDir = true))
      case f :: tail if markArg.markFile contains f => recMarkArgParser(tail, marker.copy(markFile = true))
      case lm :: tail if markArg.lastModified contains lm => recMarkArgParser(tail, marker.copy(markLm = true))
      case other => throw new IllegalArgumentException(s"Unknown parameter value for ${appArgs.mark}: ${other mkString ","}")
    }

    recMarkArgParser(split(args), TreeExMarker.default)
  }

  def parseArgs(args: Array[String]): TreeExArgs = {

    @tailrec
    def recParser(args: List[String], treeExArgs: TreeExArgs): TreeExArgs = args match {

      case Nil => treeExArgs

      case path :: value :: tail if appArgs.path contains path => recParser(tail, treeExArgs.copy(path = Some(value)))

      case find :: value :: tail if appArgs.find contains find => recParser(tail, treeExArgs.copy(findValues = split(value)))

      case ex :: value :: tail if appArgs.exclude contains ex => recParser(tail, treeExArgs.copy(exValues = split(value)))

      case mark :: value :: tail if appArgs.mark contains mark => recParser(tail, treeExArgs.copy(marker = markerFromArg(value)))

      case arg :: value :: _ if isValueIncorrect(value) => throw new IllegalArgumentException(s"Unknown argument '$arg $value'")

      case other => throw new IllegalArgumentException(s"Unknown arguments '${other.mkString("= ")}'")
    }

    recParser(args.toList, TreeExArgs(marker = TreeExMarker.default))
  }
}