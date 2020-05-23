package com.markin

import com.markin.elastic.ElasticSearchIndexComp
import com.markin.ingest.FileFeederComp
import com.markin.parser.DocumentParserComp
import com.typesafe.scalalogging.Logger

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
    pageIndex.search(searchString).fold[Unit] (
      e => logger.error(s"Page search by $searchString failed: ${e.toString}"),
      pages => pages.foreach(p => logger.debug(p.toString))
    )

    documentIndex.shutdown()
    pageIndex.shutdown()
  }
}
