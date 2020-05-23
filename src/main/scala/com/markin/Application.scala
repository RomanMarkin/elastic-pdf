package com.markin

import com.markin.elastic.elastic4s.Elastic4sIndexCompImpl
import com.markin.ingest.simple.SimpleFileFeederCompImpl
import com.markin.parser.pdfbox.PdfBoxDocumentParserCompImpl
import com.typesafe.scalalogging.Logger

object Application extends App {
  val logger: Logger = Logger(this.getClass)

  /** Provide Dependency Injection for used components */
  val docHandler = new DocumentHandler
    with PdfBoxDocumentParserCompImpl
    with SimpleFileFeederCompImpl
    with Elastic4sIndexCompImpl

  docHandler.handleDocuments()
}
