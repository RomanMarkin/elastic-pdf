package com.markin.ingest.simple

import java.io.File

import com.markin.ingest.FileFeederComp
import com.markin.util.Config

trait SimpleFileFeederCompImpl extends FileFeederComp {

  override val fileFeeder: FileFeeder = new SimpleFileFeeder

  class SimpleFileFeeder extends FileFeeder {
    private val incomingDir = Config.envOrElseString("app.ingest.incomingDir")
    private val allowedFileExtensions = Seq("pdf")

    override def getFiles: Seq[File] = getListOfFiles(new File(incomingDir), allowedFileExtensions)

    private def getListOfFiles(dir: File, extensions: Seq[String]): Seq[File] = {
      if (dir.exists)
        dir.listFiles.filter(_.isFile).toList.filter { file =>
          extensions.exists(file.getName.endsWith(_))
        } else
        Seq()
    }
  }
}
