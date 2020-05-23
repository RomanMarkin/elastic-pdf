package com.markin.builder

import java.util.UUID
import com.markin.model.Author
import AuthorBuilder._

class AuthorBuilder[A <: AuthorTemplate](author: Author =
                    Author(UUID.randomUUID(),
                      Option.empty,
                      Option.empty,
                      Option.empty)) {

  def addFirstName(firstName: String): AuthorBuilder[A with FirstName] =
    new AuthorBuilder(author.copy(firstName = Option(firstName)))

  def addMiddleName(middleName: String): AuthorBuilder[A with MiddleName] =
    new AuthorBuilder(author.copy(middleName = Option(middleName)))

  def addLastName(lastName: String): AuthorBuilder[A with LastName] =
    new AuthorBuilder(author.copy(lastName = Option(lastName)))

  def build(implicit evidence: A =:= ValidAuthor): Author = author
}

object AuthorBuilder {
  sealed trait AuthorTemplate
  sealed trait EmptyAuthor extends AuthorTemplate
  sealed trait FirstName extends AuthorTemplate
  sealed trait MiddleName extends AuthorTemplate
  sealed trait LastName extends AuthorTemplate
  type ValidAuthor = EmptyAuthor with FirstName with MiddleName with LastName
}
