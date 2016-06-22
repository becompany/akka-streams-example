package ch.becompany.akka.io.file

import java.nio.file.{Files, Path, Paths}

import akka.NotUsed
import akka.actor.{ActorSystem, Props}
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.Framing._
import akka.stream.scaladsl.{FileIO, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString

import scala.concurrent.Future

object FileReader {

  private implicit val system = ActorSystem("akka-file-io")
  private implicit val materializer = ActorMaterializer()

  /**
    * Reads a file.
    *
    * @param path The path.
    * @param encoding The encoding.
    * @return Either an error or the source.
    */
  def read(path: String, encoding: String): Source[String, Future[IOResult]] =
    FileIO.fromPath(Paths.get(path)).
      via(delimiter(ByteString("\n"), Int.MaxValue)).
      map(_.decodeString(encoding))


  /**
    * Read a file continuously.
    *
    * @param path The path.
    * @param encoding The encoding.
    * @return Either an error or the source.
    */
  def readContinuously(path: String, encoding: String): Source[String, NotUsed] = {
    val tailPublisher = system.actorOf(Props[TailPublisher])
    val src = Source.
      fromPublisher(ActorPublisher[ByteString](tailPublisher)).
      map(_.decodeString(encoding))
    new Tailer(Paths.get(path), encoding, tailPublisher).run()
    src
  }

}
