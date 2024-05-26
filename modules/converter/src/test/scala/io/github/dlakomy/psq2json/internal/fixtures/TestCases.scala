package io.github.dlakomy.psq2json.internal.fixtures

final case class TestCase(
    createTypeSql: String,
    callFnSql: String,
    resultJson: String // TODO use json type, not string (Circe?)
)


val user = TestCase(
  """create or replace type user as object (
    |  id      number
    |, name    varchar2(50)
    |, surname varchar2(100)
    |, born    date
    |);
    |/""".stripMargin,
  "user_to_json(user(1,'John','Doe',date'1984-04-19'))",
  """{"id":1,"name":"John","surname":"Doe","born":"1984-04-19T00:00:00"}"""
)


val animal = TestCase(
  """create or replace type animal as object (
    |  id      number
    |, name    varchar2(50)
    |, species varchar2(50)
    |);
    |/""".stripMargin,
  "animal_to_json(animal(1,'John','Dove'))",
  """{"id":1,"name":"John","species":"Dove"}"""
)


val TestCases = List(user, animal)
