package com.markin

import com.markin.elastic.ElasticSearchIndexComp
import com.markin.ingest.FileFeederComp
import com.markin.parser.DocumentParserComp
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success}

class DocumentHandler {
  this: DocumentParserComp
    with FileFeederComp
    with ElasticSearchIndexComp =>

  private val logger: Logger = Logger(this.getClass)

  def handleDocuments(): Unit = {
    documentIndex.delete
    pageIndex.delete

    documentIndex.create
    pageIndex.create

    val docs = for {
      file <- fileFeeder.getFiles
      doc <- documentParser.parse(file)
    } yield doc
    documentIndex.bulkIndex(docs)

    val pages = for {
      doc <- docs
      page <- doc.pages
    } yield page
    pageIndex.bulkIndex(pages)

    val searchString = "мин"
    pageIndex.search(searchString).foreach {
      case Success(page) => logger.debug(page.toString)
      case Failure(e) => logger.error(s"Page search by $searchString failed: ${e.toString}")
    }

    documentIndex.shutdown()
    pageIndex.shutdown()
  }
}
