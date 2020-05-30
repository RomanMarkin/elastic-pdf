package com.markin.elastic.elastic4s.json

import java.util.UUID

import com.markin.model.Author
import com.sksamuel.elastic4s.playjson.{playJsonIndexable, playJsonHitReader}
import com.sksamuel.elastic4s.{HitReader, Indexable}
import play.api.libs.json.{Format, Json}

case class AuthorDTO(uuid: UUID,
                     firstName: Option[String],
                     middleName: Option[String],
                     lastName: Option[String])


object AuthorDTO extends DTO[Author, AuthorDTO] {

  implicit val toDto: Author => AuthorDTO =
    (a: Author) =>
      AuthorDTO(
        a.uuid,
        a.firstName,
        a.middleName,
        a.lastName
      )

  implicit val fromDto: AuthorDTO => Author =
    (dto: AuthorDTO) =>
      Author(
        dto.uuid,
        dto.firstName,
        dto.middleName,
        dto.lastName
      )

  implicit val format: Format[AuthorDTO] = Json.format[AuthorDTO]
  implicit val indexable: Indexable[AuthorDTO] = playJsonIndexable[AuthorDTO]
  implicit val hitReader: HitReader[AuthorDTO] = playJsonHitReader[AuthorDTO]
}