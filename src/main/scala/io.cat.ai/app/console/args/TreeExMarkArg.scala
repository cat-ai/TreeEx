package io.cat.ai.app.console.args

final case class TreeExMarkArg(markDir: Boolean = false,
                               markFile: Boolean = false,
                               markLm: Boolean = false)

object TreeExMarkArg {
  def default: TreeExMarkArg = TreeExMarkArg()
}