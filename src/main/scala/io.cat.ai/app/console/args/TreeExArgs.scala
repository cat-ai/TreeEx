package io.cat.ai.app.console.args

final case class TreeExArgs(path: Option[String] = None,
                            findValues: Seq[String] = Nil,
                            exValues: Seq[String] = Nil,
                            marker: TreeExMarkArg)
