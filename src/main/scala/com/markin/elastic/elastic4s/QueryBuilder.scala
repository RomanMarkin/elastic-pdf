package com.markin.elastic.elastic4s

import com.sksamuel.elastic4s.requests.searches.queries.Query

/**
 * Provides methods to apply optional parameters to [[com.sksamuel.elastic4s.requests.searches.queries.Query]]
 * TODO It is not used yet because the complexity of usage this builder is similar with code which applies the optional parameters by itself.
 * It is expected that this builder may become useful later with the growth of usage of the `Query`.
 * @example [[com.markin.elastic.elastic4s.DocumentSearch]]
 * */
object QueryBuilder {

  /** Returns new instance of query with optionally applied parametrised condition.
   * To apply given parameter, converts its type using provided function to make it supported by query.
   *
   *  @tparam Q Type of the query.
   *  @tparam P1 Type of the given parameter before conversion.
   *  @tparam P2 Type of the given parameter after conversion.
   *  @param q Query to be used to apply a condition with optional parameter to.
   *  @param optParam Optional parameter is used to create and apply condition on query.
   *  @param convertParam Function to convert type of the parameter in order to make it supported by query.
   *  @param applyParam Function which applies converted parameter to the the query (actually it just should call certain method on the Query object).
   *  @return New query instance with optionally applied parameter. */
  def applyOptionalQueryParamWithConversion[Q <: Query, P1, P2](
    q: Q,
    optParam: Option[P1],
    convertParam: P1 => P2,
    applyParam: (Q, P2) => Q
  ): Q =
    optParam.map(convertParam) match {
      case Some(p) => applyParam(q, p)
      case None    => q
    }

  /** Returns new instance of query with optionally applied parametrised condition.
   *
   *  @tparam Q Type of the query.
   *  @tparam P Type of the given parameter.
   *  @param q Query to be used to apply a condition with optional parameter to.
   *  @param optParam Optional parameter is used to create and apply condition on the query.
   *  @param applyParam Function which applies parameter to the query (actually it just should call certain method on the Query object).
   *  @return New query instance with optionally applied parameter. */
  def applyOptionalQueryParam[Q <: Query, P](q: Q,
                                              optParam: Option[P],
                                              applyParam: (Q, P) => Q): Q =
    applyOptionalQueryParamWithConversion(q, optParam, (p: P) => p, applyParam)
}
