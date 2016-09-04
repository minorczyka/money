name := """money-server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1",
  "com.jason-goodwin" %% "authentikat-jwt" % "0.4.1"
)
