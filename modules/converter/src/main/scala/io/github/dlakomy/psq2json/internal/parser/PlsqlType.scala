package io.github.dlakomy.psq2json.internal.parser

import cats.data.NonEmptyList

private[psq2json] final case class PlsqlType(name: String, fields: NonEmptyList[PlsqlField])

/* the type distinction actually is not useful... but let's keep it;
 * the name though must be a part of PlsqlField, to avoid pattern matching
 * if it's the only thing needed...
 */
private[psq2json] enum PlsqlField(val name: String):
  case Date(override val name: String)   extends PlsqlField(name)
  case Text(override val name: String)   extends PlsqlField(name)
  case Number(override val name: String) extends PlsqlField(name)
