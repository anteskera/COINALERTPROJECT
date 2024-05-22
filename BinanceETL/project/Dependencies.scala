import sbt.*
import sbt.Keys.*

object Dependencies {

  lazy val sparkVersion = "3.5.1"

  val sparkDependency: Seq[ModuleID] = Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "compile",
    "org.apache.spark" %% "spark-sql" % sparkVersion % "compile""
  )
}