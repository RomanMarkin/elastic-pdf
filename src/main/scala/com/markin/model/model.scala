package com.markin.model

import java.util.{Date, UUID}

case class Document(uuid: UUID,
                    title: Option[String],
                    authors: Seq[String],
                    keywords: Option[String],
                    fileUrl: Option[String],
                    titleImgUrl: Option[String],
                    publishingDate: Option[Date],
                    pages: Seq[Page])

/** This companion object of Document case class provides apply() and unapply() methods without field 'pages'.
 * We need them in JSON readers/writers. */
object Document{
//  def apply(uuid: UUID,
//            title: Option[String],
//            authors: Seq[String],
//            keywords: Option[String],
//            fileUrl: Option[String],
//            titleImgUrl: Option[String],
//            publishingDate: Option[Date]): Document =
//    Document(uuid, title, authors, keywords, fileUrl, titleImgUrl, publishingDate, Seq[Page]())
//
//  def unapply(d: Document): Option[(UUID, Option[String], Seq[String], Option[String], Option[String], Option[String], Option[Date])] =
//    Option(d.uuid, d.title, d.authors, d.keywords, d.fileUrl, d.titleImgUrl, d.publishingDate)
}

case class Page(uuid: UUID,
                index: Option[Int],
                text: Option[String],
                docUuid: Option[UUID])

case class Author(uuid: UUID,
                  firstName: Option[String],
                  middleName: Option[String],
                  lastName: Option[String]) {

  def fullName: String = Seq[Option[String]](firstName, middleName, lastName).
    flatten.mkString(" ")
}

case class Library(name: String)
case class Publisher(name: String)
