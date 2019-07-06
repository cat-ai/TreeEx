package io.cat.ai.app.mode

sealed trait TreeExMode {

  def findValues: Seq[String]

  def excludeValues: Seq[String]

  def markLm: Boolean

  def markDirectories: Boolean

  def markFiles: Boolean
}

case object DefaultMode extends TreeExMode {

  override def findValues: Seq[String] = Nil

  override def excludeValues: Seq[String] = Nil

  override def markLm: Boolean = false

  override def markDirectories: Boolean = false

  override def markFiles: Boolean = false
}

final case class SpecMode(override val findValues: Seq[String],
                          override val excludeValues: Seq[String],
                          override val markLm: Boolean,
                          override val markDirectories: Boolean,
                          override val markFiles: Boolean) extends TreeExMode
