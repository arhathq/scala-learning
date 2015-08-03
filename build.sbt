name := "scala-learning"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "ch.qos.logback" % "logback-classic" % "1.1.2" % Runtime,
  "com.h2database" % "h2" % "1.3.175",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)