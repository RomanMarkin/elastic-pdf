package com.markin.elastic.elastic4s

import com.markin.elastic.ElasticSearchIndexComp
import com.markin.model.{Document, Page}
import com.markin.util.Config

import scala.util.Try

trait Elastic4sIndexCompImpl extends ElasticSearchIndexComp {

  override val documentIndex: ElasticSearchIndex[Document] = new DocumentIndex(Config.envOrElseString("app.elastic.index.document"))
  override val pageIndex: ElasticSearchIndex[Page] = new PageIndex(Config.envOrElseString("app.elastic.index.page"))

  class DocumentIndex(name: String) extends ElasticSearchIndex[Document] with Elastic4sIndex[Document] {

    import json.DocumentDTO._

    override def indexName: String = name

    override def index(document: Document): Try[String] = super[Elastic4sIndex].index(document)

    override def bulkIndex(documents: Seq[Document]): Try[String] = super[Elastic4sIndex].bulkIndex(documents)

    override def search(query: String): Seq[Try[Document]] = super[Elastic4sIndex].search(query)
  }

  class PageIndex(name: String) extends ElasticSearchIndex[Page] with Elastic4sIndex[Page] {

    import json.PageDTO._

    override def indexName: String = name

    override def index(page: Page): Try[String] = super[Elastic4sIndex].index(page)

    override def bulkIndex(pages: Seq[Page]): Try[String] = super[Elastic4sIndex].bulkIndex(pages)

    override def search(query: String): Seq[Try[Page]] = super[Elastic4sIndex].search(query)
  }
}
