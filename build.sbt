import Dependencies._

resolvers ++= Seq(
  "BeCompany Nexus" at "https://nexus.becompany.ch/content/repositories/public",
  sbtResolver.value
)

lazy val logAnalysis = (
  Project("akka-streams-example", file("."))
  settings(
    organization := "ch.becompany",
    name := "akka-streams-example",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.11.8",
    libraryDependencies ++= dependencies,
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
  )
)
