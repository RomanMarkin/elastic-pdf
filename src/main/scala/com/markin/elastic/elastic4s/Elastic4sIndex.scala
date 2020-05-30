package com.markin.elastic.elastic4s

import com.sksamuel.elastic4s.{ElasticClient, HitReader, Indexable, RequestFailure, RequestSuccess, Response}
import com.sksamuel.elastic4s.ElasticDsl.{bulk, createIndex, deleteIndex, indexInto, search}
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.typesafe.scalalogging.Logger
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.searches.SearchResponse

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

/**
 * This trait keeps most of the implementation of trait @see ElasticSearchIndex declared in @see Elastic4sIndexCompImpl.
 * It should be mixed in by other implementations of @see ElasticSearchIndex to make them using this implementation.
 *
 * @tparam M the type of the domain model object being mapped into DTO, so that DTO is saved in the Elasticsearch index.
 *
 * Read more about:
 * 1. Elastic4s Scala Client: https://index.scala-lang.org/sksamuel/elastic4s/elastic4s-play-json/5.2.4?target=_2.12
 * 2. Elastic4s Search DSL: https://github.com/sksamuel/elastic4s/blob/master/elastic4s-tests/src/test/scala/com/sksamuel/elastic4s/search/SearchDslTest.scala*
 * 3. How to parse JSON search results: https://elastic4s.readthedocs.io/en/stable/src/main/tut/docs/
 * */
trait Elastic4sIndex[M] {
  private val logger: Logger = Logger(this.getClass)
  private val client: ElasticClient = Elastic4sClientProvider.client

  def indexName: String

  def create: Try[String] = {
    val resp = client.execute {
      createIndex(indexName)
    }.await
    mapElastic4sResponse(resp)
  }

  def delete: Try[String] = {
    val resp = client.execute {
      deleteIndex(indexName)
    }.await
    mapElastic4sResponse(resp)
  }

  /**
   * @tparam D the type of the DTO for the domain model object actually being kept in the Elasticsearch index.
   * */
  def index[D : Indexable](document: M)(implicit toDTO: M => D): Try[String] = {
    val resp = client.execute {
      indexInto(indexName).doc(toDTO(document)).refresh(RefreshPolicy.Immediate)
    }.await
    mapElastic4sResponse(resp)
  }

  def bulkIndex[D : Indexable](documents: Seq[M])(implicit toDTO: M => D): Try[String] = {
    val resp = client.execute {
      bulk(
        for {
          document <- documents
        } yield indexInto(indexName).doc(toDTO(document)).refresh(RefreshPolicy.Immediate)
      )
    }.await
    mapElastic4sResponse(resp)
  }

  def search[D: HitReader : ClassTag](queryString: String)(implicit fromDTO: D => M): IndexedSeq[Try[M]] = {
    client.execute {
      com.sksamuel.elastic4s.ElasticDsl.search(indexName).query(queryString)
    }.await.result.safeTo[D].map {
      case Success(d) => Success(fromDTO(d))
      case Failure(e) => Failure(e)
    }
  }

  def refresh: Try[String] = {
    val resp = client.execute {
      refreshIndex(indexName)
    }.await
    mapElastic4sResponse(resp)
  }

  /** Closes the client. You can't reopen the client after shutdown. As soon as the client is a singleton in [[Elastic4sClientProvider]],
   * you should only close it on application shutdown.
   */
  def shutdown(): Unit = client.close()

  private def mapElastic4sResponse(response: com.sksamuel.elastic4s.Response[_]): Try[String] =
    response match {
      case _: RequestSuccess[_] => Success("Success")
      case _: RequestFailure => Failure(new Exception("Have got elastic4s response with an error"))
      case _ => Failure(new Exception("Unknown elastic4s response"))
    }
}