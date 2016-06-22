package ch.becompany

import _root_.akka.actor.ActorSystem
import _root_.akka.stream.ActorMaterializer
import ch.becompany.akka.io.csv.{Csv, CsvSpec, Parser}
import ch.becompany.akka.io.file.FileReader
import org.joda.time.Instant

import scala.util.{Success, Try}

object InstantParser extends Parser[Instant] {
  def apply(s: String): Either[String, Instant] = {
    Try(new Instant(s.toLong)).transform(
      s => Success(Right(s)),
      f => Success(Left(f.getMessage))).get
  }
}

object LogAnalyzer {

  import ch.becompany.akka.io.csv.Parsers._

  implicit val system = ActorSystem("log-analyzer")
  implicit val materializer = ActorMaterializer()

  implicit val instantParser = InstantParser
  lazy val csvSpec = CsvSpec(encoding = Some("UTF-8"))
  lazy val csv = new Csv[LogEntry](csvSpec)

  def duplicate(e1: LogEntry, e2:  LogEntry): Boolean = {
    val interval = e2.time.getMillis - e1.time.getMillis
    interval >= 0 && interval < 1000 && e1.req == e2.req
  }

  type ScanResult = (Seq[LogEntry], Option[Seq[LogEntry]])

  def duplicates(prev: ScanResult, next: LogEntry): ScanResult = {
    val (acc, result) = prev
    acc match {
      case l :+ last if duplicate(last, next) => (acc :+ next, None)
      case l :+ last => (Seq(next), if (acc.size > 1) Some(acc) else None)
      case Nil => (Seq(next), None)
    }
  }

  def output(e: LogEntry) = s"${e.ip} - ${e.time} - ${e.req}"

  def output(entries: Seq[LogEntry]): String =
    (" " ++ entries.map(output)).mkString("\n")

  def apply(path: String): Unit =
    csv.read(FileReader.readContinuously(path, "UTF-8")).
      groupBy(Int.MaxValue, entry => (entry.ip, entry.userAgent)).
      scan[ScanResult]((Seq(), None))(duplicates).
      collect { case (_, Some(entries)) => entries }.
      mergeSubstreams.
      map(output).
      runForeach(println)

}
