package io.github.dlakomy.psq2json.internal.parser

import cats.parse.{Parser as P, Rfc5234 as R}
import cats.syntax.show.*


object Parser:
  def parse(input: String): Either[String, PlsqlType] =
    import instance.given
    Helper.plsqlType.parseAll(input).left.map(_.show)


private object Helper:
  val wsp = R.wsp | R.lf | R.cr

  extension [A](p: P[A])
    def pad0: P[A] =
      p <* wsp.rep0

    def pad: P[A] =
      val wsp = (R.wsp | R.lf | R.cr)
      (p <* wsp.rep.withContext("whitespace"))

  // letter must be first, but let's simplify a bit
  val identifier = (R.alpha | R.digit | P.char('_')).rep.string

  val declaration: P[String] =
    val create    = P.ignoreCase("create").pad
    val orReplace = P.ignoreCase("or").pad ~ P.ignoreCase("replace")
    create *> orReplace.pad.? *> P.ignoreCase("type").pad *>
      identifier.pad
      <* P.ignoreCase("as").pad <* P.ignoreCase("object")

  val field: P[PlsqlField] =
    val date = P.ignoreCase("date").as(PlsqlField.Date.apply)
    // intentionally skipping other numbers and precision
    val number = P.ignoreCase("number").as(PlsqlField.Number.apply)
    val varchar2 =
      // I've skipped BYTE/CHAR keyword, I know
      P.ignoreCase("varchar2").pad0 ~ (R.digit.pad0.rep.between(P.char('(').pad0, P.char(')')))
    val text = varchar2.as(PlsqlField.Text.apply)

    val typ = date | text | number

    (identifier.pad ~ typ).map((ident, constr) => constr(ident))

  val fields = P.repSep(field.pad0, P.char(',').pad0)

  val plsqlType =
    (declaration.pad0 ~
      fields.between(P.char('(').pad0, P.char(')').pad0).pad0
      <* P.char(';').pad0 <* P.char('/').pad0.?)
      .map(PlsqlType.apply)
