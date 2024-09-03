import Dependencies.sparkDependency

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
      name := "ETL_TA",
      javaOptions += "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
      libraryDependencies ++= sparkDependency
)