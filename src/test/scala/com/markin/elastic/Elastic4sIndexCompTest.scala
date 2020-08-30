package com.markin.elastic

import java.time.LocalDate
import java.util.UUID

import com.markin.UnitSpec
import com.markin.elastic.elastic4s.Elastic4sIndexCompImpl
import com.markin.model.{Document, Page}
import org.scalatest.{BeforeAndAfterEach, Suite}

import scala.util.{Success, Try}

trait IndexFixture {

  class Elastic4sIndexComp {
    this: ElasticSearchIndexComp =>
  }

  val elasticIndexComp = new Elastic4sIndexComp with Elastic4sIndexCompImpl
  val docIdx = new elasticIndexComp.DocumentIndex("test_document")
  val pageIdx = new elasticIndexComp.PageIndex("test_page")
  val docSearch = new elasticIndexComp.DocumentIndexSearch("test_document")

  val doc0 = Document(UUID.randomUUID(), Some("Title0"), Seq("Author0", "Author9"), Some("keyword0"), Some("file0"), Some("titleImgUrl0"), Some(LocalDate.of(2020, 1, 1)), Seq())
  val doc1 = Document(UUID.randomUUID(), Some("Title1"), Seq("Author1", "Author9"), Some("keyword1"), Some("file1"), Some("titleImgUrl1"), Some(LocalDate.of(2020, 2, 1)), Seq())
  val doc2 = Document(UUID.randomUUID(), Some("Title2"), Seq("Author2", "Author9"), Some("keyword2"), Some("file2"), Some("titleImgUrl2"), Some(LocalDate.of(2020, 3, 1)), Seq())
  val doc3 = Document(UUID.randomUUID(), Some("Title2"), Seq("Author3", "Author9"), Some("keyword3"), Some("file3"), Some("titleImgUrl3"), Some(LocalDate.of(2020, 4, 1)), Seq())

  val page0 = Page(UUID.randomUUID(), Some(0), Some("Page 0 Text"), Some(doc0.uuid))
  val page1 = Page(UUID.randomUUID(), Some(1), Some("Page 1 Text"), Some(doc0.uuid))
  val page2 = Page(UUID.randomUUID(), Some(2), Some("Page 2 Text"), Some(doc0.uuid))
  val page3 = Page(UUID.randomUUID(), Some(3), Some("Page 3 Text"), Some(doc0.uuid))
}

trait BeforeAndAfterEachTest extends BeforeAndAfterEach {
  this: Suite with IndexFixture =>

  override def beforeEach() {
    docIdx.create
    pageIdx.create
    super.beforeEach()
  }

  override def afterEach() {
    try super.afterEach()
    finally {
      docIdx.delete
      pageIdx.delete
    }
  }
}

class Elastic4sIndexCompTest extends UnitSpec with IndexFixture with BeforeAndAfterEachTest {

  "the Elastic4sIndexCompImpl" should "index a document" in {
    docIdx.index(doc0) should be(Success("Success"))
  }

  "the Elastic4sIndexCompImpl" should "bulk index a sequence of documents" in {
    docIdx.bulkIndex(Seq(doc1, doc2, doc3)).isSuccess should be(true)
  }

  "the Elastic4sIndexCompImpl" should "search a document" in {
    docIdx.bulkIndex(Seq(doc1, doc2, doc3))
    docIdx.refresh // In elasticsearch documents are not ready for search immediately: https://stackoverflow.com/questions/30602459/getting-zero-results-in-search-using-elastic4s
    docIdx.search("Title1") should be(Seq(Success(doc1)))
  }

  "the Elastic4sIndexCompImpl" should "search several documents" in {
    pageIdx.bulkIndex(Seq(page0, page0, page0))
    pageIdx.refresh
    pageIdx.search("Page 0 Text") should be(Seq(Success(page0), Success(page0), Success(page0)))
  }

  "the Elastic4sIndexCompImpl" should "search documents by publishing date between given min and max dates" in {
    docIdx.bulkIndex(Seq(doc0, doc1, doc2, doc3))
    docIdx.refresh
    docSearch.searchByPublishingDate(Some(LocalDate.of(2020, 1, 15)), Some(LocalDate.of(2020, 3, 15))) should be(Seq(Success(doc1), Success(doc2)))
  }

  "the Elastic4sIndexCompImpl" should "find no documents by publishing date if given min date is greater than max" in {
    docIdx.bulkIndex(Seq(doc0, doc1, doc2, doc3))
    docIdx.refresh
    docSearch.searchByPublishingDate(Some(LocalDate.of(2020, 5, 1)), Some(LocalDate.of(2020, 1, 1))) should be(Seq())
  }

  "the Elastic4sIndexCompImpl" should "search documents by publishing date greater than given min date" in {
    docIdx.bulkIndex(Seq(doc0, doc1, doc2, doc3))
    docIdx.refresh
    docSearch.searchByPublishingDate(Some(LocalDate.of(2020, 1, 15)), None) should be(Seq(Success(doc1), Success(doc2), Success(doc3)))
  }

  "the Elastic4sIndexCompImpl" should "search documents by publishing date less than given max date" in {
    docIdx.bulkIndex(Seq(doc0, doc1, doc2, doc3))
    docIdx.refresh
    docSearch.searchByPublishingDate(None, Some(LocalDate.of(2020, 3, 15))) should be(Seq(Success(doc0), Success(doc1), Success(doc2)))
  }

  "the Elastic4sIndexCompImpl" should "search documents by keywords" in {
    docIdx.bulkIndex(Seq(doc0, doc1, doc2, doc3))
    docIdx.refresh
    docSearch.searchByKeywords(Seq("keyword0")) should be(Seq(Success(doc0)))
  }

}

