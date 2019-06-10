package io.cat.ai.app.view

final case class FileGraphWalker(nDirs: Int = 0,
                                 nFiles: Int = 0,
                                 excluded: String = "",
                                 found: String = "")