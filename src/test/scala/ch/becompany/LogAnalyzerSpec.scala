package ch.becompany

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

class LogAnalyzerSpec extends FlatSpec with Matchers with ScalaFutures {

  val testFiles = Seq()

  "LogAnalyzer" should "analyze log" in {
    testFiles foreach { fileName =>
      LogAnalyzer(s"src/test/resources/ch/post/pcc/$fileName")
    }
  }

}
