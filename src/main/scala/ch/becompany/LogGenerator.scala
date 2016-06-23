package ch.becompany

import java.net.InetAddress

import _root_.akka.stream.scaladsl.{Sink, Source}
import _root_.akka.actor.ActorSystem
import _root_.akka.stream.ActorMaterializer
import org.joda.time.Instant

import scala.concurrent.duration._
import scala.util.Random

object LogGenerator {

  implicit val system = ActorSystem("log-analyzer")
  implicit val materializer = ActorMaterializer()

  case object Tick
  val interval = 200 millis

  val rand = new Random(System.currentTimeMillis)

  def ips = Seq("1.2.3.4", "2.3.4.5", "3.4.5.6").map(InetAddress.getByName)
  def urls = Seq("/foo", "/bar", "/baz")
  def userAgents = Seq("Firefox", "Chrome")

  def rnd[T](seq: Seq[T]) = seq(rand.nextInt(seq.size))

  def createLogEntry(): LogEntry =
    LogEntry(rnd(ips), Instant.now, rnd(urls), rnd(userAgents))

  def formatLogEntry(e: LogEntry): String =
    Seq(e.ip.getHostAddress, e.time.getMillis.toString, e.req, e.userAgent).
      map(s => s""""$s"""").
      mkString(",")

  def apply(): Unit = {
    val tickSource = Source.tick(0 seconds, interval, Tick)
    val logSource = Source.fromIterator(() => Iterator.continually(createLogEntry))
    Source.
      zipN(List(logSource, tickSource)).
      map(_.head.asInstanceOf[LogEntry]).
      map(formatLogEntry).
      runWith(Sink.foreach(println))
  }

}
