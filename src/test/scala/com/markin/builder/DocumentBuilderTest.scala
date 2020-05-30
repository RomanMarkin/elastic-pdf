package com.markin.builder

import java.util.UUID

import com.markin.UnitSpec
import com.markin.model.{Document, Page}

class DocumentBuilderTest extends UnitSpec {

  "the Document builder" should "build a valid empty document" in {
    val document = new DocumentBuilder[DocumentBuilder.EmptyDocument]().
      addTitle("Title").
      addAuthor("Author").
      addKeywords("Keywords").
      buildEmpty
    document should be(Document(document.uuid, Some("Title"), Seq("Author"), Some("Keywords"), None, None, None, Seq()))
  }

  "the Document builder" should "append authors" in {
    val document = new DocumentBuilder[DocumentBuilder.EmptyDocument]().
      addTitle("Title").
      addAuthor("Author1").
      addAuthor("Author2").
      addAuthor("Author3").
      addKeywords("Keywords").
      buildEmpty
    document should be(Document(document.uuid, Some("Title"), Seq("Author1", "Author2", "Author3"), Some("Keywords"), None, None, None, Seq()))
  }

  "the Document builder" should "build a valid full document" in {
    val emptyDoc =
      Document(UUID.randomUUID(), Some("Title"), Seq("Author"), Some("Keywords"), None, None, None, Seq())

    val pages = Seq(
      Page(UUID.randomUUID(), Some(0), Some("Page No0 Text"), Some(emptyDoc.uuid)),
      Page(UUID.randomUUID(), Some(1), Some("Page No1 Text"), Some(emptyDoc.uuid)),
      Page(UUID.randomUUID(), Some(2), Some("Page No2 Text"), Some(emptyDoc.uuid))
    )

    val fullDoc = new DocumentBuilder[DocumentBuilder.ValidEmptyDocument](emptyDoc).
      addPages(pages).
      build

    fullDoc should be(emptyDoc.copy(pages =
      Seq(
        Page(fullDoc.pages(0).uuid, Some(0), Some("Page No0 Text"), Some(emptyDoc.uuid)),
        Page(fullDoc.pages(1).uuid, Some(1), Some("Page No1 Text"), Some(emptyDoc.uuid)),
        Page(fullDoc.pages(2).uuid, Some(2), Some("Page No2 Text"), Some(emptyDoc.uuid))
      )))
  }

  "the Document builder" should "replace pages in addPages" in {
    val emptyDoc =
      Document(UUID.randomUUID(), Some("Title"), Seq("Author"), Some("Keywords"), None, None, None, Seq())

    val pages1 = Seq(
      Page(UUID.randomUUID(), Some(0), Some("Page No0 Text"), Some(emptyDoc.uuid)),
      Page(UUID.randomUUID(), Some(1), Some("Page No1 Text"), Some(emptyDoc.uuid)),
      Page(UUID.randomUUID(), Some(2), Some("Page No2 Text"), Some(emptyDoc.uuid))
    )

    val pages2 = Seq(
      Page(UUID.randomUUID(), Some(100), Some("Page No100 Text"), Some(emptyDoc.uuid)),
      Page(UUID.randomUUID(), Some(101), Some("Page No101 Text"), Some(emptyDoc.uuid))
    )

    val fullDoc1 = new DocumentBuilder[DocumentBuilder.ValidEmptyDocument](emptyDoc).
      addPages(pages1).
      build

    val fullDoc2 = new DocumentBuilder[DocumentBuilder.ValidDocument](fullDoc1).
      addPages(pages2).
      build

    fullDoc2 should be(emptyDoc.copy(pages = pages2))
  }

  "the Document builder" should "append pages in appendPages" in {
    val emptyDoc =
      Document(UUID.randomUUID(), Some("Title"), Seq("Author"), Some("Keywords"), None, None, None, Seq())

    val pages1 = Seq(
      Page(UUID.randomUUID(), Some(0), Some("Page No0 Text"), Some(emptyDoc.uuid)),
      Page(UUID.randomUUID(), Some(1), Some("Page No1 Text"), Some(emptyDoc.uuid)),
      Page(UUID.randomUUID(), Some(2), Some("Page No2 Text"), Some(emptyDoc.uuid))
    )

    val pages2 = Seq(
      Page(UUID.randomUUID(), Some(100), Some("Page No100 Text"), Some(emptyDoc.uuid)),
      Page(UUID.randomUUID(), Some(101), Some("Page No101 Text"), Some(emptyDoc.uuid))
    )

    val fullDoc1 = new DocumentBuilder[DocumentBuilder.ValidEmptyDocument](emptyDoc).
      addPages(pages1).
      build

    val fullDoc2 = new DocumentBuilder[DocumentBuilder.ValidDocument](fullDoc1).
      appendPages(pages2).
      build

    fullDoc2 should be(emptyDoc.copy(pages = pages1 ++ pages2))
  }

}
