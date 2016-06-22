package ch.becompany

import java.io.File

import scopt.OptionParser

object LogAnalysis extends App {

  object Commands {
    sealed trait Command
    case object Generate extends Command
    case object Analyze extends Command
  }

  import Commands._

  case class Config(cmd: Command = null, file: File = null)

  val parser = new OptionParser[Config]("log-analysis.sh") {
    head("log-analysis", "1.0.0-SNAPSHOT")

    cmd("generate").action((_, c) => c.copy(cmd = Generate)).
      text("Generate log file.")

    cmd("analyze").action((_, c) => c.copy(cmd = Analyze)).
      text("Analyze log file.").children(
        arg[File]("<file>") required() action { (v, c) =>
          c.copy(file = v)
        } validate { f =>
          if (f.isFile) success else failure("File does not exist")
        } text("Path to log file")
    )
  }

  for {
    config <- parser.parse(args, Config())
  } yield {
    config.cmd match {
      case Generate => LogGenerator()
      case Analyze => LogAnalyzer(config.file.getAbsolutePath)
    }
  }

}
