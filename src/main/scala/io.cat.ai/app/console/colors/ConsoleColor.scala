package io.cat.ai.app.console.colors

sealed trait ConsoleColor {

  def color: String

  def bold: String

  def underlined: String

  def reset: String = "\033[0m"
}

case object Black extends ConsoleColor {

  override def color: String = "\033[0;30m"

  override def bold: String = "\033[1;30m"

  override def underlined: String = "\033[4;30m"
}

case object Red extends ConsoleColor {

  override def color: String = "\033[0;31m"

  override def bold: String = "\033[1;31m"

  override def underlined: String = "\033[4;31m"
}

case object Green extends ConsoleColor {

  override def color: String = "\033[0;32m"

  override def bold: String = "\033[1;32m"

  override def underlined: String = "\033[4;32m"
}

case object Yellow extends ConsoleColor {

  override def color: String = "\033[0;33m"

  override def bold: String = "\033[1;32m"

  override def underlined: String = "\033[4;33m"
}

case object Blue extends ConsoleColor {

  override def color: String = "\033[0;34m"

  override def bold: String = "\033[1;34m"

  override def underlined: String = "\033[4;34m"
}

case object White extends ConsoleColor {

  override def color: String = "\033[0;37m"

  override def bold: String = "\033[1;37m"

  override def underlined: String = "\033[4;37m"
}