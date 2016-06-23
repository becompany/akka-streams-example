package ch.becompany.akka.io.csv

import org.scalatest._

class LineParserSpec extends FlatSpec with Matchers with EitherValues {

  import Parsers._

  case class Record(a: String, b: String)

  "A LineParser" should "parse lines" in {
    val line = List("foo", "bar")
    val result = LineParser[Record](line)
    result.right.value should be (Record("foo", "bar"))
  }

  "A LineParser" should "fail on missing elements" in {
    val line = List("foo")
    val result = LineParser[Record](line)
    result.left.value should be (List("Excepted list element."))
  }

  "A LineParser" should "fail on surplus elements" in {
    val line = List("foo", "bar", "baz")
    val result = LineParser[Record](line)
    result.left.value should be (List("""Expected end, got "baz"."""))
  }

}
