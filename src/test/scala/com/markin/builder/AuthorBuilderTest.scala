package com.markin.builder

import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import com.markin.model.Author

class AuthorBuilderTest extends AnyFlatSpec with Matchers {

  "the Author builder" should "build a valid author" in {
    val author = new AuthorBuilder[AuthorBuilder.EmptyAuthor]().
      addFirstName("FirstName").
      addMiddleName("MiddleName").
      addLastName("LastName").
      build
    author should be (Author(author.uuid, Some("FirstName"), Some("MiddleName"), Some("LastName")))
  }

  "the Author builder" should "rewrite attributes" in {
    val author = new AuthorBuilder[AuthorBuilder.EmptyAuthor]().
      addFirstName("FirstName").
      addFirstName("FirstName2").
      addMiddleName("MiddleName").
      addMiddleName("MiddleName2").
      addLastName("LastName").
      addLastName("LastName2").
      build
    author should be (Author(author.uuid, Some("FirstName2"), Some("MiddleName2"), Some("LastName2")))
  }

}
