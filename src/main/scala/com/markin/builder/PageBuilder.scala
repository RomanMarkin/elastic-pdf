package com.markin.builder

import java.util.UUID
import com.markin.model.Page
import PageBuilder._

class PageBuilder[P <: PageTemplate](page: Page =
                  Page(UUID.randomUUID(),
                    Option.empty,
                    Option.empty,
                    Option.empty)) {

  def addIndex(index: Int):PageBuilder[P with Index] = new PageBuilder(page.copy(index = Option(index)))

  def addText(text: String):PageBuilder[P with Text] = new PageBuilder(page.copy(text = Option(text)))

  def addDocUuid(docUuid: UUID):PageBuilder[P with DocUuid] = new PageBuilder(page.copy(docUuid = Option(docUuid)))

  def build(implicit evidence: P =:= ValidPage): Page = page

}

object PageBuilder {
  /** Phantom Types */
  sealed trait PageTemplate
  sealed trait EmptyPage extends PageTemplate
  sealed trait Index extends PageTemplate
  sealed trait Text extends PageTemplate
  sealed trait DocUuid extends PageTemplate
  type ValidPage = EmptyPage with Index with Text with DocUuid
}
