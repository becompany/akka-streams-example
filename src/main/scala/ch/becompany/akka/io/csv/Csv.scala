package ch.becompany.akka.io.csv

import akka.stream.scaladsl.Source
import com.github.tototoshi.csv.{CSVParser, DefaultCSVFormat}

class CsvReader[T : LineParser] {

  private lazy val csvParser = new CSVParser(new DefaultCSVFormat() {})

  private def parseLine(line: String): Either[List[String], T] = {
    csvParser.parseLine(line) match {
      case Some(fields) => LineParser[T](fields)
      case None => Left(List(s"Invalid line: $line"))
    }
  }

  /**
    * Transforms a flow of strings into a flow of CSV records.
    * Invalid records will be skipped.
    *
    * @param source The source.
    * @tparam Mat The materialized value type.
    * @return The transformed source.
    */
  def read[Mat](source: Source[String, Mat]): Source[T, Mat] =
    source.
      map(parseLine).
      map(_.fold(errors => { println(errors); None }, Some(_))).
      collect { case Some(t) => t }

}
