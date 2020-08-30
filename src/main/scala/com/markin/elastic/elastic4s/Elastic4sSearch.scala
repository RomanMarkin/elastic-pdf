package com.markin.elastic.elastic4s

import com.sksamuel.elastic4s.{ElasticClient, HitReader}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.searches.SearchRequest
import com.typesafe.scalalogging.Logger

/** @tparam M the type of the domain model object being mapped into DTO, so that DTO is fetched from the Elasticsearch index. */
trait Elastic4sSearch[M] {
  private val logger: Logger = Logger(this.getClass)
  private val client: ElasticClient = Elastic4sClientProvider.client

  def execSearch[D: HitReader : ClassTag](searchRequest: SearchRequest)(implicit fromDTO: D => M): IndexedSeq[Try[M]] = {
    client.execute(searchRequest).
      await.result.safeTo[D].map {
      case Success(d) => Success(fromDTO(d))
      case Failure(e) => Failure(e)
    }
  }

}
