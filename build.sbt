import org.apache.logging.log4j.core.config.plugins.processor.PluginCache
import sbtassembly.MergeStrategy

import java.io.FileOutputStream
import scala.jdk.CollectionConverters.asJavaEnumerationConverter

ThisBuild / scalaVersion     := "2.12.15"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.varokas"
ThisBuild / organizationName := "mleapdemo"

lazy val root = (project in file("."))
  .settings(
    name := "MLeap Demo",
  )

libraryDependencies ++= Seq(
  "ml.combust.mleap" %% "mleap-runtime" % "0.18.0",

  "com.amazonaws" % "aws-lambda-java-core" % "1.2.1",
  "com.amazonaws" % "aws-lambda-java-events" % "3.10.0",

  "org.apache.logging.log4j" % "log4j-api" % "2.13.2",
  "org.apache.logging.log4j" % "log4j-core" % "2.13.2",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.13.0",
  "com.amazonaws" % "aws-lambda-java-log4j2" % "1.2.0",
)

// https://github.com/marccarre/log4j2-sbt-assembly
// https://github.com/idio/sbt-assembly-log4j2

val Log4J2PlugInCacheMergeStrategy = new MergeStrategy {
  val name = "log4j2::plugincache"
  def apply(tempDir: File, path: String, files: Seq[File]): Either[String, Seq[(File, String)]] = {
    val file = MergeStrategy.createMergeTarget(tempDir, path)
    val out = new FileOutputStream(file)

    val aggregator = new PluginCache()
    val filesEnum = files.toIterator.map(_.toURI.toURL).asJavaEnumeration

    try {
      aggregator.loadCacheFiles(filesEnum)
      aggregator.writeCache(out)
      Right(Seq(file -> path))
    }
    finally {
      out.close()
    }
  }
}

ThisBuild / assemblyMergeStrategy := {
  case PathList(ps @ _*) if ps.last == "Log4j2Plugins.dat" => Log4J2PlugInCacheMergeStrategy
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}

assembly / assemblyOutputPath := file("target/function.jar")