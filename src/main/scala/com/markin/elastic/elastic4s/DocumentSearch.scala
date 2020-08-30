package com.markin.elastic.elastic4s

import java.time.LocalDate

import com.markin.model.Document
import com.sksamuel.elastic4s.ElasticDate

import scala.util.{Failure, Success, Try}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.searches.queries.{Query, RangeQuery}
import com.sksamuel.elastic4s.requests.searches.queries.term.TermsQuery

// TODO don't use strings for index field names. Replace them with code values are used for index generation

/**
 * This trait keeps most of the implementation of trait [[com.markin.elastic.ElasticSearchIndexComp.DocumentSearchAPI]].
 * It should be mixed in by other implementations of `DocumentSearchAPI` to make them using this implementation.
*/
trait DocumentSearch extends Elastic4sSearch[Document]{

  def indexName: String

  import json.DocumentDTO._

  def searchByPublishingDate(minDate: Option[LocalDate], maxDate: Option[LocalDate]): Seq[Try[Document]] =
    execSearch {

      val applyLte: RangeQuery => RangeQuery =
        rq => maxDate.map(ElasticDate(_)) match {
          case Some(d) => rq.lte(d)
          case None => rq
        }

      val applyGte: RangeQuery => RangeQuery =
        rq => minDate.map(ElasticDate(_)) match {
          case Some(d) => rq.gte(d)
          case None => rq
        }

/*      val applyGteV2: RangeQuery => RangeQuery =
        rq => QueryBuilder.
          applyOptionalQueryParamWithConversion[RangeQuery, LocalDate, ElasticDate](rq,
            minDate,
            ElasticDate(_),
            (rq, v) => rq.gte(v))*/

      val buildQuery = applyGte andThen applyLte

      val rangeQuery = buildQuery(RangeQuery("publishingDate"))
      search(indexName).query(rangeQuery)
    }


  def searchByKeywords(keywords: Seq[String]): Seq[Try[Document]] =
    execSearch{
      search(indexName).query(TermsQuery("keywords", keywords))
    }
}
