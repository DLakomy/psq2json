package io.github.dlakomy.psq2json.internal.parser.instance

import cats.Show
import cats.parse.LocationMap
import cats.parse.Parser.Expectation.*
import cats.parse.Parser.{Error, Expectation}
import cats.syntax.show.*


// the main change is that context doesn't mention details and there is a deduplication step after "show"
given Show[Expectation] with
  private val dq = "\""
  def show(expectation: Expectation): String = expectation match
    case OneOfStr(_, strs: List[String]) =>
      if (strs.lengthCompare(1) > 0) {
        "must match one of the strings: " + strs.iterator
          .map(s => dq + s + dq)
          .mkString("{", ", ", "}")
      } else if (strs.nonEmpty) {
        "must match string: " + dq + strs.head + dq
      } else
        "??? bug with Expectation.OneOfStr"

    case InRange(_, lower: Char, upper: Char) =>
      if (lower != upper) s"must be a char within the range of: ['$lower', '$upper']"
      else s"must be char: '$lower'"

    case StartOfString(_) =>
      "must start the string"

    case EndOfString(_, _) =>
      s"must end the string"

    case Length(_, expected, actual) =>
      s"must have a length of $expected but got a length of $actual"

    case ExpectedFailureAt(_, matched) =>
      s"must fail but matched with $matched"

    case Fail(_) =>
      "must fail"

    case FailWith(_, message) =>
      s"must fail: $message"

    // changed
    case WithContext(contextStr: String, _) =>
      contextStr


given Show[Error] with
  def show(error: Error): String =
    val nl = "\n"

    def errorMsg =
      val expectations =
        error.expected.toList.iterator
          .map(e => show"* $e")
          .distinct // my addition
          .mkString(nl)

      s"""|expectation${if (error.expected.tail.nonEmpty) "s" else ""}:
          |$expectations""".stripMargin

    error.input match
      case Some(input) =>
        val locationMap = new LocationMap(input)

        locationMap.toCaret(error.failedAtOffset) match
          case None => errorMsg
          case Some(caret) =>
            val lines: Array[String] = input.split("\n", -1)

            val contextSize = 2

            val start = caret.line - contextSize
            val end   = caret.line + 1 + contextSize

            val elipsis = "..."

            val beforeElipsis =
              if (start <= 0) None
              else Some(elipsis)

            val beforeContext =
              Some(lines.slice(start, caret.line).mkString(nl)).filter(_.nonEmpty)

            val line = lines(caret.line)

            val afterContext: Option[String] =
              Some(lines.slice(caret.line + 1, end).mkString(nl)).filter(_.nonEmpty)

            val afterElipsis: Option[String] =
              if (end >= lines.length - 1) None
              else Some(elipsis)

            List(
              beforeElipsis,
              beforeContext,
              Some(line),
              Some((1 to caret.col).map(_ => " ").mkString("") + "^"),
              Some(errorMsg),
              afterContext,
              afterElipsis
            ).flatten.mkString(nl)
      case None =>
        s"""|at offset ${error.failedAtOffset}
            |$errorMsg""".stripMargin
