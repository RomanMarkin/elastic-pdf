package com.markin.elastic.elastic4s.json

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import play.api.libs.json._

/**
 * Provides Play's Formats for read/write Scala objects from/to JSON.
 * */
object CommonFormats {
  private val dateFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd")

  implicit val dateFormat: Format[Date] = new Format[Date] {
    override def writes(d: Date): JsValue = JsString(dateFormatter.format(d))
    override def reads(json: JsValue): JsResult[Date] = json.validate[String].map[Date](dString => dateFormatter.parse(dString))
  }

  implicit val uuidFormat: Format[UUID] = new Format[UUID] {
    override def writes(uuid: UUID): JsValue = JsString(uuid.toString)
    override def reads(json: JsValue): JsResult[UUID] = json.validate[String].map[UUID](uuidString => UUID.fromString(uuidString))
  }
}
