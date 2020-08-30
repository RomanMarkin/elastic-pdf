package com.markin.elastic

import java.time.LocalDate

import com.markin.model.{Document, Page}

import scala.util.Try

trait ElasticSearchIndexComp {

  val documentIndex: ElasticSearchIndex[Document]
  val pageIndex: ElasticSearchIndex[Page]
  val documentIndexSearch: DocumentSearchAPI

  /** Basic API for Elasticsearch indexes */
  trait ElasticSearchIndex[T] {
    def create: Try[String]
    def delete: Try[String]
    def index(document: T): Try[String]
    def bulkIndex(documents: Seq[T]): Try[String]
    def search(query: String): Seq[Try[T]]
    def refresh: Try[String]
    def shutdown(): Unit
  }

  /** Search API for Elasticsearch index of type Document */
  trait DocumentSearchAPI{
    def searchByPublishingDate(minDate: Option[LocalDate], maxDate: Option[LocalDate]): Seq[Try[Document]]
    def searchByKeywords(keywords: Seq[String]): Seq[Try[Document]]
  }

}
