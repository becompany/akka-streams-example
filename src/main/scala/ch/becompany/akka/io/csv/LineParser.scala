package ch.becompany.akka.io.csv

import shapeless.{::, Generic, HList, HNil}

trait LineParser[T] {
  def apply(l: List[String]): Either[List[String], T]
}

object LineParser {

  implicit val hnilParser: LineParser[HNil] = new LineParser[HNil] {
    def apply(s: List[String]): Either[List[String], HNil] =
      s match {
        case Nil => Right(HNil)
        case h +: t => Left(List(s"""Expected end of line, got "$h"."""))
      }
  }

  implicit def hconsParser[H: Parser, T <: HList : LineParser]: LineParser[H :: T] =
    new LineParser[H :: T] {
      def apply(s: List[String]): Either[List[String], H :: T] = s match {
        case h +: t => {
          val head = implicitly[Parser[H]].apply(h)
          val tail = implicitly[LineParser[T]].apply(t)
          (head, tail) match {
            case (Left(error), Left(errors)) => Left(error :: errors)
            case (Left(error), Right(_)) => Left(error :: Nil)
            case (Right(_), Left(errors)) => Left(errors)
            case (Right(h), Right(t)) => Right(h :: t)
          }
        }
      }
    }

  implicit def caseClassParser[A, R <: HList]
  (implicit gen: Generic[A] { type Repr = R }, reprParser: LineParser[R]): LineParser[A] =
    new LineParser[A] {
      def apply(s: List[String]): Either[List[String], A] =
        reprParser.apply(s).right.map(gen.from)
    }

  def apply[A](s: List[String])(implicit parser: LineParser[A]): Either[List[String], A] = parser(s)
}