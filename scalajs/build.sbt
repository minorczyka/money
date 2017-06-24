enablePlugins(ScalaJSPlugin)
enablePlugins(WorkbenchPlugin)

name := "money"

version := "1.0"

scalaVersion := "2.12.1"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "com.github.japgolly.scalajs-react" %%% "core" % "1.0.1",
  "com.github.japgolly.scalajs-react" %%% "extra" % "1.0.1",
  "io.suzaku" %%% "diode" % "1.1.2",
  "io.suzaku" %%% "diode-react" % "1.1.2",
  "io.circe" %%% "circe-core" % "0.8.0",
  "io.circe" %%% "circe-generic" % "0.8.0",
  "io.circe" %%% "circe-parser" % "0.8.0"
)

jsDependencies ++= Seq(
  "org.webjars.bower" % "react" % "15.5.4" / "react-with-addons.js" commonJSName "React" minified "react-with-addons.min.js",
  "org.webjars.bower" % "react" % "15.5.4" / "react-dom.js" commonJSName "ReactDOM" minified "react-dom.min.js" dependsOn "react-with-addons.js"
)
