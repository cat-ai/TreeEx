package io.cat.ai.app

sealed trait TreeExMode {

  def value: String

  def excludingValue: Option[String]

  def markLm: Boolean

  def markDirectories: Boolean

  def markFiles: Boolean
}

case object DefaultMode extends TreeExMode {

  override def value: String = excludingValue

  override def markLm: Boolean = false

  override def markDirectories: Boolean = false

  override def markFiles: Boolean = false

  override def excludingValue: Nothing = throw new NoSuchElementException("Excluding value: <empty>")
}

case class FindMode(override val value: String,
                    override val excludingValue: Option[String],
                    override val markLm: Boolean,
                    override val markDirectories: Boolean = false,
                    override val markFiles: Boolean = false) extends TreeExMode

case class FindAndMarkMode(override val value: String,
                           override val excludingValue: Option[String],
                           override val markLm: Boolean,
                           override val markDirectories: Boolean,
                           override val markFiles: Boolean) extends TreeExMode
