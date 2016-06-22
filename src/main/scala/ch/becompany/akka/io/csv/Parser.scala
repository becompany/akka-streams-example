package ch.becompany.akka.io.csv

trait Parser[T] {
  def apply(s: String): Either[String, T]
}

object Parsers {

  implicit val stringParser: Parser[String] = new Parser[String] {
    def apply(s: String): Either[String, String] = Right(s)
  }

}