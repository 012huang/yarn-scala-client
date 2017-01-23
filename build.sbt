organization := "pl.jaceklaskowski.yarn"
name         := "yarn-scala-client"
version      := "1.0.0"

scalaVersion := "2.12.1"

val yarnVer = "2.7.3"
Seq("client", "yarn-client", "yarn-api").map { name =>
  libraryDependencies += "org.apache.hadoop" % s"hadoop-$name" % yarnVer
}
