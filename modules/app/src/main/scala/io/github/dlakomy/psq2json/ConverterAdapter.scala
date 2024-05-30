package io.github.dlakomy.psq2json

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}


@JSExportTopLevel("Converter")
object ConverterAdapter:
  @JSExport
  def convert(input: String) =
    Converter.convert(input) match
      case Left(errorMsg) =>
        new js.Object:
          val error = errorMsg
      case Right(plsqlCode) =>
        new js.Object:
          val result = plsqlCode
