package io.cat.ai.app.console

import io.cat.ai.app.config.TreeExConfig
import io.cat.ai.app.console.args.{TreeExArgs, TreeExMarkArg}

import scala.annotation.tailrec

final class DefaultTreeExCLI(override val conf: TreeExConfig) extends TreeExCLI[TreeExArgs, Seq[String]] {

  object markArg {
    val markDir: Option[String]      = conf.argValue("mark", "markDir")
    val markFile: Option[String]     = conf.argValue("mark", "markFile")
    val lastModified: Option[String] = conf.argValue("mark", "markLm")
  }

  val isValueIncorrect: String => Boolean = _ startsWith "-"

  val splitToList: (String, String) => Seq[String] = _ split _

  val split: String => Seq[String]  = splitToList(_, conf.getClArgSplitter)

  def markerFromArg(args: String): TreeExMarkArg = {

    @tailrec
    def recMarkArgParser(markArgs: Seq[String],
                         marker: TreeExMarkArg): TreeExMarkArg = markArgs match {
      case Nil => marker

      case d  +: tail if markArg.markDir contains d       => recMarkArgParser(tail, marker.copy(markDir = true))
      case f  +: tail if markArg.markFile contains f      => recMarkArgParser(tail, marker.copy(markFile = true))
      case lm +: tail if markArg.lastModified contains lm => recMarkArgParser(tail, marker.copy(markLm = true))

      case other => throw new IllegalArgumentException(s"Unknown parameter value for ${appArgs.mark}: ${other mkString ","}")
    }

    recMarkArgParser(split(args), TreeExMarkArg.default)
  }

  override def parseArgs(args: Seq[String]): TreeExArgs = {

    @tailrec
    def recParser(args: Seq[String],
                  treeExArgs: TreeExArgs): TreeExArgs = args match {
      case Nil => treeExArgs

      case path +: value +: tail if appArgs.path contains path  => recParser(tail, treeExArgs.copy(path = Some(value)))
      case find +: value +: tail if appArgs.find contains find  => recParser(tail, treeExArgs.copy(findValues = split(value)))
      case ex   +: value +: tail if appArgs.exclude contains ex => recParser(tail, treeExArgs.copy(exValues = split(value)))
      case mark +: value +: tail if appArgs.mark contains mark  => recParser(tail, treeExArgs.copy(marker = markerFromArg(value)))
      case arg  +: value +: _    if isValueIncorrect(value)     => throw new IllegalArgumentException(s"Unknown argument '$arg $value'")

      case unknown => throw new IllegalArgumentException(s"Unknown arguments '${unknown.mkString("= ")}'")
    }

    recParser(args, TreeExArgs(marker = TreeExMarkArg.default))
  }

}