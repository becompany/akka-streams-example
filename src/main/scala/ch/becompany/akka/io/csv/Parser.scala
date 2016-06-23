package ch.becompany.akka.io.csv

import scala.util.{Success, Try}

trait Parser[T] {
  def apply(s: String): Either[String, T]
}

trait TryParser[T] extends Parser[T] {

  protected def parse(s: String): T

  def apply(s: String): Either[String, T] =
    Try(parse(s)).transform(
      s => Success(Right(s)),
      f => Success(Left(f.getMessage))).get
}

object Parsers {

  implicit val stringParser: Parser[String] = new Parser[String] {
    def apply(s: String): Either[String, String] = Right(s)
  }

}