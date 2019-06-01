package io.cat.ai.app.args

case class TreeExArgs(path: Option[String] = None,
                      findValues: List[String] = Nil,
                      exValues: List[String] = Nil,
                      marker: TreeExMarker)
