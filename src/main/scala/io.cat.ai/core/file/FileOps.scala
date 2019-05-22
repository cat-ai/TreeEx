package io.cat.ai.core.file

import java.io.File
import java.util.Date

object FileOps {

  implicit class FileAsOpt(file: File) {
    def opt: Option[File] = Option(file)
  }

  implicit class FileArrAsOpt(files: Array[File]) {
    def asOpt: Option[Array[File]] = Option(files)
  }

  implicit class SafeListFile(file: File) {
    def safeListFiles: List[File] = Option(file.listFiles) map(_ toList) getOrElse Nil
  }

  implicit class FileLastModified(file: File) {
    def lastModifiedDate: String = s"${new Date(file.lastModified)}"
  }
}
