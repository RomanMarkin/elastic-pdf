package com.markin.util

import com.typesafe.config.ConfigFactory

import scala.util.Properties

object Config {

  private val config: com.typesafe.config.Config = ConfigFactory.load()

  def envOrElseString(name: String): String = {
    Properties.envOrElse(
      name.toUpperCase.replaceAll("""\.""", "_"),
      config.getString(name)
    )
  }

  def envOrElseInt(name: String): Option[Int] = {
    import scala.util.control.Exception._
    allCatch.opt(envOrElseString(name).toInt)
  }

  def envOrElseBoolean(name: String): Boolean = {
    envOrElseString(name).trim.toLowerCase() match {
      case "true" => true
      case _      => false
    }
  }

}
