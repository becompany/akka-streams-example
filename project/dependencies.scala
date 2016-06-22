import sbt._

object Dependencies {
  
  val scalaTestVersion = "2.2.6"
  val slf4jVersion = "1.7.19"
  val scoptVersion = "3.4.0"
  val akkaVersion = "2.4.7"
  val jodaTimeVersion = "2.9.4"
  val commonsIoVersion = "2.5"
  val shapelessVersion = "2.3.1"
  val scalaCsvVersion = "1.3.1"

  val slf4j = "org.slf4j" % "slf4j-nop" % slf4jVersion
  val scalactic = "org.scalactic" %% "scalactic" % scalaTestVersion
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  val scopt = "com.github.scopt" %% "scopt" % scoptVersion
  val akkaStreams = "com.typesafe.akka" % "akka-stream_2.11" % akkaVersion
  val jodaTime = "joda-time" % "joda-time" % jodaTimeVersion
  val commonsIo = "commons-io" % "commons-io" % commonsIoVersion
  val shapeless = "com.chuusai" %% "shapeless" % shapelessVersion
  val scalaCsv = "com.github.tototoshi" %% "scala-csv" % scalaCsvVersion

  val dependencies = Seq(
    slf4j,
    scalactic,
    scalaTest,
    scopt,
    akkaStreams,
    jodaTime,
    commonsIo,
    shapeless,
    scalaCsv
  )

}
