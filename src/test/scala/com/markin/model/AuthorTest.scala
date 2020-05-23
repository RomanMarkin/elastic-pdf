package com.markin.model

import java.util.UUID

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AuthorTest extends AnyFlatSpec with Matchers {

  "the Author model" should "return valid full name" in {
    val author = Author(UUID.randomUUID(), Some("FirstName"), Some("MiddleName"), Some("LastName"))
    author.fullName should be ("FirstName MiddleName LastName")
  }

  it should "return full name from just first and last names" in {
    val author = Author(UUID.randomUUID(), Some("FirstName"), None, Some("LastName"))
    author.fullName should be ("FirstName LastName")
  }

  it should "return full name from just first name" in {
    val author = Author(UUID.randomUUID(), Some("FirstName"), None, None)
    author.fullName should be ("FirstName")
  }

  it should "return full name from just last name" in {
    val author = Author(UUID.randomUUID(), None, None, Some("LastName"))
    author.fullName should be ("LastName")
  }

  it should "return full name from just middle name" in {
    val author = Author(UUID.randomUUID(), None, Some("MiddleName"), None)
    author.fullName should be ("MiddleName")
  }

}
