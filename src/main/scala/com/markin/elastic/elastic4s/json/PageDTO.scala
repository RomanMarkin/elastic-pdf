package com.markin.elastic.elastic4s.json

import java.util.UUID

import com.markin.model.Page
import com.sksamuel.elastic4s.{HitReader, Indexable}
import com.sksamuel.elastic4s.playjson.{playJsonHitReader, playJsonIndexable}
import play.api.libs.json.{Format, Json}

case class PageDTO(uuid: UUID,
                   index: Option[Int],
                   text: Option[String],
                   docUuid: Option[UUID])

object PageDTO extends DTO[Page, PageDTO]{

  implicit val toDto: Page => PageDTO =
    (p: Page) =>
      PageDTO(
        p.uuid,
        p.index,
        p.text,
        p.docUuid
      )

  implicit val fromDto: PageDTO => Page =
    (dto: PageDTO) =>
      Page(
        dto.uuid,
        dto.index,
        dto.text,
        dto.docUuid
      )

  implicit val format: Format[PageDTO] = Json.format[PageDTO]
  implicit val indexable: Indexable[PageDTO] = playJsonIndexable[PageDTO]
  implicit val hitReader: HitReader[PageDTO] = playJsonHitReader[PageDTO]
}