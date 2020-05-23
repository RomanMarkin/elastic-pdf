package com.markin.ingest

import java.io.File

trait FileFeederComp {

  val fileFeeder: FileFeeder

  trait FileFeeder {
    def getFiles: Seq[File]
  }
}
