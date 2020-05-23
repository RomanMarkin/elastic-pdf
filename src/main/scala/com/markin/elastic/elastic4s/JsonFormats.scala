package com.markin.elastic.elastic4s

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import com.markin.model.{Author, Document, Page}
import com.sksamuel.elastic4s.{HitReader, Indexable}
import com.sksamuel.elastic4s.playjson.{playJsonHitReader, playJsonIndexable}
import play.api.libs.json._
import play.api.libs.json.DefaultFormat
import play.api.libs.functional.syntax._

object JsonFormats {
  private val dateFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd")

  implicit val dateFormat: Format[Date] = new Format[Date] {
    override def writes(d: Date): JsValue = JsString(dateFormatter.format(d))
    override def reads(json: JsValue): JsResult[Date] = json.validate[String].map[Date](dString => dateFormatter.parse(dString))
  }

  implicit val uuidFormat: Format[UUID] = new Format[UUID] {
    override def writes(uuid: UUID): JsValue = JsString(uuid.toString)
    override def reads(json: JsValue): JsResult[UUID] = json.validate[String].map[UUID](uuidString => UUID.fromString(uuidString))
  }

  implicit def optionFormat[T: Format]: Format[Option[T]] = new Format[Option[T]]{
    override def reads(json: JsValue): JsResult[Option[T]] = json.validateOpt[T]

    override def writes(o: Option[T]): JsValue = o match {
      case Some(t) => implicitly[Writes[T]].writes(t)
      case None => JsNull
    }
  }

  implicit def seqFormat[T: Format]: Format[Seq[T]] = new Format[Seq[T]]{
    override def reads(json: JsValue): JsResult[Seq[T]] = ???
    override def writes(o: Seq[T]): JsValue = ???
  }

  //implicit val stringSeqFormat: Format[Seq[String]] = Json.format[Seq[String]]
  implicit val authorFormat: Format[Author] = Json.format[Author]
  implicit val pageFormat: Format[Page] = Json.format[Page]
  //implicit val pagesSeqFormat: Format[Seq[Page]] = Json.format[Seq[Page]]

  implicit val documentFormat: Format[Document] = (
      (JsPath \ "uuid").format[UUID] and
      (JsPath \ "title").format[Option[String]] and
      (JsPath \ "authors").format[Seq[String]] and
      (JsPath \ "keywords").format[Option[String]] and
      (JsPath \ "fileUrl").format[Option[String]] and
      (JsPath \ "titleImgUrl").format[Option[String]] and
      (JsPath \ "publishingDate").format[Option[Date]] and
      (JsPath \ "pages").format[Seq[Page]]
  )(Document.apply, unlift(Document.unapply))



// TODO make ToJson, FromJson import JsonFormats.

//  implicit val pageIndexable: Indexable[Page] = playJsonIndexable[Page]
//  implicit val authorIndexable: Indexable[Author] = playJsonIndexable[Author]
//  implicit val documentIndexable: Indexable[Document] = playJsonIndexable[Document]
//
//  /** HitReader Typeclass: @see https://github.com/sksamuel/elastic4s/#hitreader-typeclass */
//  implicit val pageReader: HitReader[Page] = playJsonHitReader[Page]
//  implicit val authorReader: HitReader[Author] = playJsonHitReader[Author]
//  implicit val documentReader: HitReader[Document] = playJsonHitReader[Document]

}
