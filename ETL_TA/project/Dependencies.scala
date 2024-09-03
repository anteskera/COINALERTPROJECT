import sbt.*

object Dependencies {

  lazy val sparkVersion = "3.5.1"
  lazy val psqlVersion = "42.7.3"
  lazy val configVersion = "1.4.3"

  val sparkDependency: Seq[ModuleID] = Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "compile",
    "org.apache.spark" %% "spark-sql" % sparkVersion % "compile",
    "org.postgresql" % "postgresql" % psqlVersion,
    "com.typesafe" % "config" % configVersion
  )
}