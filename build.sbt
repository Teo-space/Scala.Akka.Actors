ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.5.0"

lazy val root = (project in file("."))
    .settings(
        name := "Scala.Akka.Actors",
        libraryDependencies ++= Seq(
            "com.typesafe.akka" %% "akka-http" % "10.5.3",
            "com.typesafe.akka" %% "akka-actor" % "2.8.6",
            "com.typesafe.akka" %% "akka-stream" % "2.8.6",
        )
    )