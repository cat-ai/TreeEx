package io.cat.ai.console

object CLI {

  object key {
    val find = "-find"
    val show = "-show"
  }

  object param {
    val dir = "dir"
    val file = "file"
    val lm = "lm"
  }

  // TODO: refactor
  def handleArgs(args: Array[String]): Params = args match {

    case Array() => throw new IllegalArgumentException("Empty program arguments!")

    case Array(x) =>
      if (x != "-p") throw new IllegalArgumentException("Param '-p' mandatory")
      else throw new UnsupportedOperationException("Not implemented yet!")

    case Array(key, path) => if (key == "-p") Params(path, path) else throw new IllegalArgumentException("Param '-p' mandatory")

    case arr => throw new UnsupportedOperationException("Not implemented yet!")
  }


}
