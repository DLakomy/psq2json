package io.github.dlakomy.psq2json

import io.github.dlakomy.psq2json.internal.parser.{Parser, PlsqlType}

object Converter:
  /** @param plsqlTypeDefinition
    *   "create or replace type..." statement
    * @return
    *   an error message or a create function PL/SQL statement;
    *   this function serializes the type to json and returns it
    *   as clob
    */
  def convert(plsqlTypeDefinition: String): Either[String, String] =
    Parser.parse(plsqlTypeDefinition).map(generateFn)

  private def generateFn(ast: PlsqlType): String =
    val typeName = ast.name
    val fields = ast.fields.toList
      .map: fld =>
        val name = fld.name
        s"  l_obj.put('$name', p_$typeName.$name);"
      .mkString("\n")

    s"""create or replace function ${typeName}_to_json(p_$typeName $typeName)
       |return clob
       |as
       |  l_obj json_object_t := json_object_t();
       |begin
       |$fields
       |  return l_obj.to_clob;
       |end;""".stripMargin
