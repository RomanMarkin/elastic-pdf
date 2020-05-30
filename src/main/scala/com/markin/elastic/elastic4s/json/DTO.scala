package com.markin.elastic.elastic4s.json

/**
  * Provides basic implementation of DTO classes used in JSON read/write operations
  *
  * @tparam M the type of the domain object bound to its DTO
  * @tparam D the type of the DTO of domain object
  * */
trait DTO[M, D] {
  implicit val toDto: M => D
  implicit val fromDto: D => M
}
