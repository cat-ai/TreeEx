package io.cat.ai.console

import io.cat.ai.app._

object CLI {

  object keys {
    val path = "-p"
    val help = "-h"
    val find = "-find"
    val mark = "-mark"
    val exclude = "-ex"
  }

  object param {
    val dir = "dir"
    val file = "file"
    val lastModified = "lm"
  }

  private val markWithLm: (String, Array[String]) => FindAndMarkMode = (value, args) => args match {

    case Array(_ @ param.dir, _ @ param.lastModified) => FindAndMarkMode(value, None, markDirectories = true, markFiles = false, markLm = true)

    case Array (_ @ param.file, _ @ param.lastModified) => FindAndMarkMode(value, None, markDirectories = false, markFiles = true, markLm = true)
  }

  private val excludeAndMark: (String, String) => FindAndMarkMode = (value, markArgs) => markArgs match {

    case _ @ param.dir => FindAndMarkMode(value, None, markLm = false, markDirectories = true, markFiles = false)

    case _ @ param.file => FindAndMarkMode(value, None, markLm = false, markDirectories = false, markFiles = true)

    case _ @ param.lastModified => FindAndMarkMode(value, None, markLm = true, markDirectories = false, markFiles = false)

    case multi if multi contains "&" => markWithLm(value, multi split "&")

    case _ => throw new IllegalArgumentException(s"Unknown arguments for ${keys.mark}")
  }

  private val modeFromParams: List[String] => TreeExMode = {

    case _ @ keys.find  :: value :: Nil => FindMode(value, None, markLm = false)

    case List(_ @ keys.find, value, _ @ keys.mark, _ @ param.lastModified) => FindMode(value, None, markLm = true)

    case List(_ @ keys.find, value, _ @ keys.exclude, exValue) => FindMode(value, Some(exValue), markLm = false)

    case List(_ @ keys.find, value, _ @ keys.mark, markArgs) => excludeAndMark(value, markArgs)

    case List(_ @ keys.find, value, _ @ keys.exclude, exValue, _ @ keys.mark, markArgs) =>
      excludeAndMark(value, markArgs).copy(excludingValue = Some(exValue))

    case other => throw new IllegalArgumentException(s"Unknown arguments: ${other mkString ","}")
  }

  def pathAndMode(args: Array[String]): (String, TreeExMode) = args.toList match {

      case Nil => throw new IllegalArgumentException("Empty program arguments")

      case _ @ keys.path :: path :: Nil => (path, DefaultMode)

      case _ @ keys.path :: path :: params => (path, modeFromParams(params))

      case other => throw new IllegalArgumentException(s"Unknown arguments: ${other.mkString}")
    }
}