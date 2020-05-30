package com.markin.elastic

import com.markin.model.{Document, Page}

import scala.util.Try

trait ElasticSearchIndexComp {

  val documentIndex: ElasticSearchIndex[Document]
  val pageIndex: ElasticSearchIndex[Page]

  trait ElasticSearchIndex[T] {
    def create: Try[String]
    def delete: Try[String]
    def index(document: T): Try[String]
    def bulkIndex(documents: Seq[T]): Try[String]
    def search(query: String): Seq[Try[T]]
    def shutdown(): Unit
  }

}
