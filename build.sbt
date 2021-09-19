ThisBuild / scalaVersion     := "2.12.15"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.varokas"
ThisBuild / organizationName := "mleapdemo"

lazy val root = (project in file("."))
  .settings(
    name := "MLeap Demo",
  )

libraryDependencies += "ml.combust.mleap" %% "mleap-runtime" % "0.18.0"
