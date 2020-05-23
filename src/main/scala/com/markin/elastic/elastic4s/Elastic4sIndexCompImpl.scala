package com.markin.elastic.elastic4s

import com.markin.elastic.ElasticSearchIndexComp
import com.markin.model.{Document, Page}
import com.markin.util.Config

import scala.util.Try

trait Elastic4sIndexCompImpl extends ElasticSearchIndexComp{

  override val documentIndex: ElasticSearchIndex[Document] = DocumentIndex
  override val pageIndex: ElasticSearchIndex[Page] = PageIndex

  object DocumentIndex extends ElasticSearchIndex[Document] with Elastic4sIndex[Document]{
    override def indexName: String = Config.envOrElseString("app.elastic.index.document")

    override def index(document: Document): Try[String] = {
      import ToJson._
      super[Elastic4sIndex].index(document)
    }

    override def bulkIndex(documents: Seq[Document]): Try[String] = {
      import ToJson._
      super[Elastic4sIndex].bulkIndex(documents)
    }

    override def search(query: String):Try[Seq[Document]] = {
      import FromJson._
      super[Elastic4sIndex].search(query)
    }
  }

  object PageIndex extends ElasticSearchIndex[Page] with Elastic4sIndex[Page]{
    override def indexName: String = Config.envOrElseString("app.elastic.index.page")

    override def index(page: Page): Try[String] = {
      import ToJson._
      super[Elastic4sIndex].index(page)
    }

    override def bulkIndex(pages: Seq[Page]): Try[String] = {
      import ToJson._
      super[Elastic4sIndex].bulkIndex(pages)
    }

    override def search(query: String):Try[Seq[Page]] = {
      import FromJson._
      super[Elastic4sIndex].search(query)
    }

  }
}