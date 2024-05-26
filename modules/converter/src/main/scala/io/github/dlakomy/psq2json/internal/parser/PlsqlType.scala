package io.github.dlakomy.psq2json.internal.parser

import cats.data.NonEmptyList

private[psq2json] final case class PlsqlType(name: String, fields: NonEmptyList[PlsqlField])

private[psq2json] enum PlsqlField:
  case Date(name: String)
  case Text(name: String)
  case Number(name: String)
