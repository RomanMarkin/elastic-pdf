package com.markin.elastic.elastic4s.json

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

import play.api.libs.json._

/**
 * Provides Play's `Formats` for read/write Scala objects from/to JSON.
 * */
object CommonFormats {
  private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

  implicit val dateFormat: Format[LocalDate] = new Format[LocalDate] {
    override def writes(d: LocalDate): JsValue = JsString(d.format(dateFormatter))
    override def reads(json: JsValue): JsResult[LocalDate] = json.validate[String].map[LocalDate](dString => LocalDate.parse(dString, dateFormatter))
  }

  implicit val uuidFormat: Format[UUID] = new Format[UUID] {
    override def writes(uuid: UUID): JsValue = JsString(uuid.toString)
    override def reads(json: JsValue): JsResult[UUID] = json.validate[String].map[UUID](uuidString => UUID.fromString(uuidString))
  }
}
