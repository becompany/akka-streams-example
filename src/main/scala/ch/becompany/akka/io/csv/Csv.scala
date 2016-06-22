package ch.becompany.akka.io.csv

import akka.stream.scaladsl.{FlowOps, Source}
import com.github.tototoshi.csv.{CSVParser, DefaultCSVFormat, QUOTE_MINIMAL, Quoting}

class Csv[T](spec: CsvSpec = CsvSpec())(implicit parser: LineParser[T]) {

  private lazy val lineParser = new CSVParser(new DefaultCSVFormat() {
    override val delimiter: Char = spec.fieldDelimiter
    override val quoteChar: Char = spec.quote
    override val quoting: Quoting = QUOTE_MINIMAL
  })

  private def parseLine(line: String): Either[List[String], T] = {
    lineParser.parseLine(line) match {
      case Some(fields) => LineParser[T](fields.map(_.trim))
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
      map(_.fold[Option[T]](errors => { println(errors); None }, Some(_))).
      collect { case Some(t) => t }

}
