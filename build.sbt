name := """play-reactivemongo-joda"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.16.2-play27"
//libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.6.13"
libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.7.1"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
