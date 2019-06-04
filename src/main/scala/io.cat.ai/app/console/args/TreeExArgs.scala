package io.cat.ai.app.console.args

final case class TreeExArgs(path: Option[String] = None,
                            findValues: List[String] = Nil,
                            exValues: List[String] = Nil,
                            marker: TreeExMarker)
