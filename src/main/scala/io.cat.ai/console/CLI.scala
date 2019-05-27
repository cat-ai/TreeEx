package io.cat.ai.console

import io.cat.ai.app._

import scala.annotation.tailrec
import scala.language.postfixOps

object CLI {

  object appArg {
    val path = "-p"
    val find = "-find"
    val mark = "-mark"
    val exclude = "-ex"
  }

  object markParam {
    val dir = "dir"
    val file = "file"
    val lastModified = "lm"
  }

  val isValueWrong: String => Boolean = _ startsWith "-"

  val splitToList: String => List[String] = arg => (arg split ",") toList

  def markerFromArg(args: String): TreeExMarker = {

    @tailrec
    def recMarkArgParser(markArgs: List[String],
                         treeExMarker: TreeExMarker): TreeExMarker = markArgs match {

      case Nil => treeExMarker
      case _ @ markParam.dir :: tail => recMarkArgParser(tail, treeExMarker.copy(markDir = true))
      case _ @ markParam.file :: tail => recMarkArgParser(tail, treeExMarker.copy(markFile = true))
      case _ @ markParam.lastModified :: tail => recMarkArgParser(tail, treeExMarker.copy(markLm = true))
      case other => throw new IllegalArgumentException(s"Unknown parameter value for ${appArg.mark}: ${other mkString ","}")
    }

    recMarkArgParser((args split ",") toList, TreeExMarker.default)
  }

  def parseArgs(args: Array[String]): TreeExArgs = {

    @tailrec
    def recParser(args: List[String], treeExArgs: TreeExArgs): TreeExArgs = args match {

      case Nil => treeExArgs

      case (_ @ appArg.path) :: value :: tail => recParser(tail, treeExArgs.copy(path = Some(value)))

      case (_ @ appArg.find) :: value :: tail => recParser(tail, treeExArgs.copy(findValues = splitToList(value)))

      case (_ @ appArg.exclude) :: value :: tail => recParser(tail, treeExArgs.copy(exValues = splitToList(value)))

      case (_ @ appArg.mark) :: value :: tail => recParser(tail, treeExArgs.copy(marker = markerFromArg(value)))

      case param :: value :: _ if isValueWrong(value) => throw new IllegalArgumentException(s"Unknown argument '$param $value'")

      case other => throw new IllegalArgumentException(s"Unknown arguments '${other.mkString("= ")}'")
    }

    recParser(args.toList, TreeExArgs(marker = TreeExMarker.default))
  }
}