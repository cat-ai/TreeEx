package io.cat.ai.app.mode

sealed trait TreeExMode {

  def findValues: List[String]

  def excludeValues: List[String]

  def markLm: Boolean

  def markDirectories: Boolean

  def markFiles: Boolean
}

case object DefaultMode extends TreeExMode {

  override def findValues: List[String] = Nil

  override def markLm: Boolean = false

  override def markDirectories: Boolean = false

  override def markFiles: Boolean = false

  override def excludeValues: List[String] = Nil
}

case class SpecMode(override val findValues: List[String],
                    override val excludeValues: List[String],
                    override val markLm: Boolean,
                    override val markDirectories: Boolean,
                    override val markFiles: Boolean) extends TreeExMode
