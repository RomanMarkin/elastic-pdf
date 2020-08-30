package com.markin.elastic.elastic4s.json

import java.time.LocalDate
import java.util.UUID

import com.markin.model.Document
import com.sksamuel.elastic4s.{HitReader, Indexable}
import com.sksamuel.elastic4s.playjson.{playJsonHitReader, playJsonIndexable}
import play.api.libs.json.{Format, Json}

case class DocumentDTO(uuid: UUID,
                       title: Option[String],
                       authors: Seq[String],
                       keywords: Option[String],
                       fileUrl: Option[String],
                       titleImgUrl: Option[String],
                       publishingDate: Option[LocalDate])

object DocumentDTO extends DTO[Document, DocumentDTO]{

  /**
   * Implicit conversion from domain model object to DTO
   * */
  implicit val toDto: Document => DocumentDTO =
    (d: Document) =>
      DocumentDTO(
        d.uuid,
        d.title,
        d.authors,
        d.keywords,
        d.fileUrl,
        d.titleImgUrl,
        d.publishingDate
    )

  /**
   * Implicit conversion provided from DTO to domain model object
   * */
  implicit val fromDto: DocumentDTO => Document =
    (dto: DocumentDTO) =>
      Document(
        dto.uuid,
        dto.title,
        dto.authors,
        dto.keywords,
        dto.fileUrl,
        dto.titleImgUrl,
        dto.publishingDate,
        Seq()
    )

  implicit val format: Format[DocumentDTO] = Json.format[DocumentDTO]

  /**
   * Indexagble typeclasses.
   * [[https://github.com/sksamuel/elastic4s/#indexable-typeclass Read more]]
   * */
  implicit val indexable: Indexable[DocumentDTO] = playJsonIndexable[DocumentDTO]


  /**
   * HitReader typeclasses.
   * [[https://github.com/sksamuel/elastic4s/#hitreader-typeclass Read more]]
   * */
  implicit val hitReader: HitReader[DocumentDTO] = playJsonHitReader[DocumentDTO]


}
