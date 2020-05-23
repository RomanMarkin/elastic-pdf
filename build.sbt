name := "elastic_pdf"

version := "0.1"

scalaVersion := "2.13.2"

val elastic4sVersion = "7.6.0"
libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,     // for the default http client
  "com.sksamuel.elastic4s" %% "elastic4s-http-streams" % elastic4sVersion,      // if you want to use reactive streams
  "com.sksamuel.elastic4s" %% "elastic4s-json-play" % elastic4sVersion,         // to convert indexing object to json
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test",  // testing
  "org.apache.pdfbox" % "pdfbox" % "2.0.19",
  //"org.scalacheck" %% "scalacheck" % "1.14.3",
  //"joda-time" % "joda-time" % "2.0",
  //"org.slf4j" % "slf4j-api" % "1.7.30",
  //"org.slf4j" % "slf4j-simple" % "1.7.30",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
