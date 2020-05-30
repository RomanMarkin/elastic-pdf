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
