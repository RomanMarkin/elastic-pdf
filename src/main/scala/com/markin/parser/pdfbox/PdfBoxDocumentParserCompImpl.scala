package com.markin.parser.pdfbox

import java.io.{File, IOException}

import com.markin.builder.{DocumentBuilder, PageBuilder}
import com.markin.model.{Document, Page}
import com.markin.parser.DocumentParserComp
import com.typesafe.scalalogging.Logger
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

trait PdfBoxDocumentParserCompImpl extends DocumentParserComp {

  override val documentParser: DocumentParser = new PdfBoxDocumentParser

  class PdfBoxDocumentParser extends DocumentParser {
    private val logger: Logger = Logger(this.getClass)

    override def parse(pdfFile: File): Option[Document] = {
      try {
        val pdDoc: PDDocument = PDDocument.load(pdfFile)
        logger.debug(s"Extracting PDF file ${pdfFile.getName}")

        withValidPermissions(pdDoc) {
          val stripper = new PDFTextStripper

          // This example uses sorting, but in some cases it is more useful to switch it off,
          // e.g. in some files with columns where the PDF content stream respects the
          // column order.
          stripper.setSortByPosition(true)

          val dInfo = pdDoc.getDocumentInformation
          val emptyDoc = new DocumentBuilder[DocumentBuilder.EmptyDocument]().
            addTitle(dInfo.getTitle).
            addAuthor(dInfo.getAuthor).
            addKeywords(dInfo.getKeywords).
            buildEmpty

          import scala.collection.mutable.ListBuffer
          val pages = new ListBuffer[Page]()

          for (pageIdx <- 0 to pdDoc.getNumberOfPages) {
            stripper.setStartPage(pageIdx)
            stripper.setEndPage(pageIdx)
            pages += new PageBuilder[PageBuilder.EmptyPage]().
              addIndex(pageIdx).
              addText(stripper.getText(pdDoc)).
              addDocUuid(emptyDoc.uuid).
              build
          }
          pdDoc.close()

          val docWithPages = new DocumentBuilder[DocumentBuilder.ValidEmptyDocument](emptyDoc).
            addPages(pages.toList).
            build

          Some(docWithPages)
        }
      } catch {
        case e: IOException => {
          logger.error(s"Failed to parse PDF file: ${e.toString}")
          Option.empty
        }
      }
    }

    private def withValidPermissions(doc: PDDocument)(func: => Option[Document]): Option[Document] =
      if (doc.getCurrentAccessPermission.canExtractContent) func else {
        logger.error("You do not have permission to extract text from this PDF")
        Option.empty
      }
  }

}
