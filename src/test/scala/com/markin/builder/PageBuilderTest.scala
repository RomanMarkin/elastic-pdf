package com.markin.builder

import java.util.UUID

import com.markin.model.Page
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PageBuilderTest extends AnyFlatSpec with Matchers {

  "the Page builder" should "build a valid page" in {
    val page = new PageBuilder[PageBuilder.EmptyPage]().
      addIndex(0).
      addText("Page Text").
      addDocUuid(UUID.randomUUID()).
      build
    page should be(Page(page.uuid, Some(0), Some("Page Text"), page.docUuid))
  }

  "the Page builder" should "rewrite attributes" in {
    val page = new PageBuilder[PageBuilder.EmptyPage]().
      addIndex(0).
      addIndex(1).
      addText("Page Text 1").
      addText("Page Text 2").
      addDocUuid(UUID.randomUUID()).
      addDocUuid(UUID.randomUUID()).
      build
    page should be(Page(page.uuid, Some(1), Some("Page Text 2"), page.docUuid))
  }

}
