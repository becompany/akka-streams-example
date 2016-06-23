package ch.becompany

import java.net.InetAddress

import org.joda.time.Instant

case class LogEntry(
  ip: InetAddress,
  time: Instant,
  req: String,
  userAgent: String
)
