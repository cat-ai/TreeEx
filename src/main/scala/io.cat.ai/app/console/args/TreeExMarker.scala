package io.cat.ai.app.console.args

final case class TreeExMarker(markDir: Boolean = false,
                              markFile: Boolean = false,
                              markLm: Boolean = false)

object TreeExMarker {
  def default: TreeExMarker = TreeExMarker()
}