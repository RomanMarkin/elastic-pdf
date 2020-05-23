package com.markin.builder

import java.util.UUID

import com.markin.model.{Document, Page}
import DocumentBuilder._

class DocumentBuilder[D <: DocumentTemplate](doc: Document =
                      Document(UUID.randomUUID(),
                        Option.empty,
                        Seq(),
                        Option.empty,
                        Option.empty,
                        Option.empty,
                        Option.empty,
                        Seq())) {

  def addTitle(title: String): DocumentBuilder[D with Title] = new DocumentBuilder(doc.copy(title = Option(title)))

  def addAuthor(author: String): DocumentBuilder[D with Author] = new DocumentBuilder(doc.copy(authors = doc.authors :+ author))

  def addAuthors(authors: Seq[String]): DocumentBuilder[D with Author] = new DocumentBuilder(doc.copy(authors = doc.authors ++ authors))

  def addKeywords(keywords: String): DocumentBuilder[D with Keywords] = new DocumentBuilder(doc.copy(keywords = Option(keywords)))

  def addPages(pages: Seq[Page]): DocumentBuilder[D with Pages] = new DocumentBuilder(doc.copy(pages = pages))

  def appendPages(pages: Seq[Page]): DocumentBuilder[D with Pages] = new DocumentBuilder(doc.copy(pages = doc.pages ++ pages))

  def buildEmpty(implicit evidence: D =:= ValidEmptyDocument): Document = doc

  def build(implicit evidence: D =:= ValidDocument): Document = doc

}

object DocumentBuilder {
  /** Phantom Types: https://medium.com/@maximilianofelice/builder-pattern-in-scala-with-phantom-types-3e29a167e863 */
  sealed trait DocumentTemplate
  sealed trait EmptyDocument extends DocumentTemplate
  sealed trait Title extends DocumentTemplate
  sealed trait Author extends DocumentTemplate
  sealed trait Keywords extends DocumentTemplate
  sealed trait Pages extends DocumentTemplate
  type ValidEmptyDocument = EmptyDocument with Title with Author with Keywords
  type ValidDocument = EmptyDocument with Title with Author with Keywords with Pages
}
