package ch.becompany.akka.io.csv

import scala.util.matching.Regex

/**
  *
  * @param lineDelimiter Line delimiter string.
  * @param fieldDelimiter Field delimiter regex.
  * @param char Quote character; None = no quoting.
  * @param encoding Encoding; None = auto-detect.
  */
case class CsvSpec(
  lineDelimiter: String = "\n",
  fieldDelimiter: Char = ',',
  quote: Char = '"',
  encoding: Option[String] = None
)
