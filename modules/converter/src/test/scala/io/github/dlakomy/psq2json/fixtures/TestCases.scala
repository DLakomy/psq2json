package io.github.dlakomy.psq2json.fixtures

import io.circe.Json
import io.circe.parser.parse


final case class TestCase(
    name: String,
    createTypeSql: String,
    callFnSql: String,
    resultJson: Json
)


val userObj = TestCase(
  "user_obj",
  """create or replace type user_obj as object (
    |  id      number
    |, name    varchar2(50)
    |, surname varchar2(100)
    |, born    date
    |);""".stripMargin,
  "user_obj_to_json(user_obj(1,'John','Doe',date'1984-04-19'))",
  // wrong order on purpose
  parse("""{"id":1,"name":"John","born":"1984-04-19T00:00:00","surname":"Doe"}""").toOption.get
)


val animal = TestCase(
  "animal",
  """create or replace type animal as object (
    |  id      number
    |, name    varchar2(50)
    |, species varchar2(50)
    |);""".stripMargin,
  "animal_to_json(animal(1,'John','Dove'))",
  parse("""{"id":1,"name":"John","species":"Dove"}""").toOption.get
)


val TestCases = List(userObj, animal)
