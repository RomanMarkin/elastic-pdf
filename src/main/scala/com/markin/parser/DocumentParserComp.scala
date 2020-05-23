package com.markin.parser

import java.io.File
import com.markin.model.Document

trait DocumentParserComp {

  val documentParser: DocumentParser

  trait DocumentParser {
    def parse(file: File): Option[Document]
  }

}
