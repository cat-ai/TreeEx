package io.cat.ai.app

import io.cat.ai.console.CLI
import io.cat.ai.core.view.FileGraphView

object TreeEx {

  def pathAndMode(args: Array[String]): (String, TreeExMode) = CLI parseArgs args match {

    case TreeExArgs(None, _, _, _) => throw new IllegalArgumentException(s"Missing argument parameter ${CLI.appArg.path}")

    case TreeExArgs(Some(value), Nil, Nil, TreeExMarker(false, false, false)) => value -> DefaultMode

    case TreeExArgs(Some(value), Nil, Nil, marker) => value -> SpecifiedMode(Nil, Nil, marker.markLm, marker.markDir, marker.markFile)

    case TreeExArgs(Some(value), findValues, Nil, marker) => value -> SpecifiedMode(findValues, Nil, marker.markLm, marker.markDir, marker.markFile)

    case TreeExArgs(Some(value), findValues, exValues, marker) => value -> SpecifiedMode(findValues, exValues, marker.markLm, marker.markDir, marker.markFile)
  }

  def view: FileGraphView = FileGraphView.default
}
