package com.markin.elastic.elastic4s

import com.markin.model.{Author, Document, Page}
import com.sksamuel.elastic4s.HitReader
import com.sksamuel.elastic4s.playjson.playJsonHitReader
import play.api.libs.json.{Json, Reads}

object FromJson {

  implicit val pageReads: Reads[Page] = Json.reads[Page]
  implicit val authorReads: Reads[Author] = Json.reads[Author]
  implicit val documentReads: Reads[Document] = Json.reads[Document]

  /** HitReader Typeclass: @see https://github.com/sksamuel/elastic4s/#hitreader-typeclass */
  implicit val pageReader: HitReader[Page] = playJsonHitReader[Page]
  implicit val authorReader: HitReader[Author] = playJsonHitReader[Author]
  implicit val documentReader: HitReader[Document] = playJsonHitReader[Document]

}
