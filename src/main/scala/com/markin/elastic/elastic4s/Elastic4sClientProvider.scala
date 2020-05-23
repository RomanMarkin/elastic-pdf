package com.markin.elastic.elastic4s

import com.markin.util.Config
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}

object Elastic4sClientProvider {

  lazy val client = ElasticClient(
    JavaClient(
      ElasticProperties(
        s"${Config.envOrElseString("app.elastic.protocol")}://${Config.envOrElseString("app.elastic.hosts")}:${Config.envOrElseString("app.elastic.port")}"
      )
    )
  )
}
