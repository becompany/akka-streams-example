package ch.becompany

import org.joda.time.Instant

case class LogEntry(
  ip: String,
  time: Instant,
  req: String,
  userAgent: String
)
