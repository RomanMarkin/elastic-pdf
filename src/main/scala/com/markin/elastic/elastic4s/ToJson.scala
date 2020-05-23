package com.markin.elastic.elastic4s

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import com.markin.model.{Author, Document, Page}
import com.sksamuel.elastic4s.Indexable
import com.sksamuel.elastic4s.playjson.playJsonIndexable
import play.api.libs.json._

object ToJson {
  private val dateFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd")

  implicit val dateWrites: Writes[Date] = (d: Date) => JsString(dateFormatter.format(d))
  implicit val uuidWrites: Writes[UUID] = (uuid: UUID) => JsString(uuid.toString)
  implicit val authorWrites: Writes[Author] = Json.writes[Author]
  implicit val pageWrites: Writes[Page] = Json.writes[Page]
  implicit val documentWrites: Writes[Document] = new Writes[Document] {
    override def writes(doc: Document): JsValue = {
      /** Do it manually to avoid rendering of the document pages into JSON. They are stored in a separate index. */
      JsObject(Seq(
        ("uuid", Json.toJson(doc.uuid)),
        ("title", Json.toJson(doc.title)),
        ("authors", Json.toJson(doc.authors)),
        ("keywords", Json.toJson(doc.keywords)),
        ("fileUrl", Json.toJson(doc.fileUrl)),
        ("titleImgUrl", Json.toJson(doc.titleImgUrl)),
        ("publishingDate", Json.toJson(doc.publishingDate))
      ))
    }
  }

  implicit val pageIndexable: Indexable[Page] = playJsonIndexable[Page]
  implicit val authorIndexable: Indexable[Author] = playJsonIndexable[Author]
  implicit val documentIndexable: Indexable[Document] = playJsonIndexable[Document]

// Alternative way to define Indexable objects:
//
//  implicit object PageIndexable extends Indexable[Page] {
//    override def json(p: Page): String = Json.stringify(Json.toJson(p))
//  }
//
//  implicit object AuthorIndexable extends Indexable[Author] {
//    override def json(a: Author): String = Json.stringify(Json.toJson(a))
//  }
//
//  implicit object DocumentIndexable extends Indexable[Document] {
//    override def json(d: Document): String = Json.stringify(Json.toJson(d))
//  }

}
