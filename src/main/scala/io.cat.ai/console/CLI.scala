package io.cat.ai.console

import io.cat.ai.app._

object CLI {

  object keys {
    val path = "-p"
    val help = "-h"
    val find = "-find"
    val mark = "-mark"
  }

  object param {
    val dir = "dir"
    val file = "file"
    val lastModified = "lm"
  }

  private def modeWithMultiArgs(value: String, args: Array[String]): TreeExMode = args match {

    case Array(_ @ param.dir, _ @ param.lastModified) => FindAndMarkMode(value, markDirectories = true, markFiles = false, markLm = true)

    case Array (_ @ param.file, _ @ param.lastModified) => FindAndMarkMode(value, markDirectories = false, markFiles = true, markLm = true)
  }

  private val modeFromParams: List[String] => TreeExMode = {

    case _ @ keys.find  :: value :: Nil => FindMode(value, markLm = false)

    case List(_ @ keys.find, value, _ @ keys.mark, _ @ param.lastModified) => FindMode(value, markLm = true)

    case List(_ @ keys.find, value, _ @ keys.mark, showArgs) => showArgs match {

      case _ @ param.dir => FindAndMarkMode(value, markLm = false, markDirectories = true, markFiles = false)

      case _ @ param.file => FindAndMarkMode(value, markLm = false, markDirectories = false, markFiles = true)

      case multi if multi contains "&" => modeWithMultiArgs(value, multi split "&")

      case _ => throw new IllegalArgumentException(s"Unknown arguments for ${keys.mark}")
    }

    case other => throw new IllegalArgumentException(s"Unknown arguments: ${other mkString ","}")
  }

  def pathAndMode(args: Array[String]): (String, TreeExMode) = args.toList match {

      case Nil => throw new IllegalArgumentException("Empty program arguments")

      case _ @ keys.path :: path :: Nil => (path, DefaultMode)

      case _ @ keys.path :: path :: params => (path, modeFromParams(params))

      case other => throw new IllegalArgumentException(s"Unknown arguments: ${other.mkString}")
    }
}