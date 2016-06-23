package ch.becompany

import java.net.InetAddress

import ch.becompany.akka.io.csv.TryParser
import org.joda.time.Instant

/**
  * Additional parsers for parsing web server logs.
  */
object LogParsers {

  implicit val instantParser = new TryParser[Instant] {
    protected def parse(s: String): Instant = new Instant(s.toLong)
  }

  implicit val inetAddressParser = new TryParser[InetAddress] {
    protected def parse(s: String) = InetAddress.getByName(s)
  }

}
