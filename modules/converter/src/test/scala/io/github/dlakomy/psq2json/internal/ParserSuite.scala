package io.github.dlakomy.psq2json.internal.parser

import cats.data.NonEmptyList

class ParserSuite extends munit.FunSuite:
  test("should parse sample type") {

    // lack of margin is intentional, to make it even messier
    // more tests should be written in the future
    val input =
      """create or repLace type something as object (
           id number,
           name varchar2(50 
           )
         ,  surname varchar2( 100  )
         , born
          date
      );"""

    val obtained = Parser.parse(input)
    val expected = Right(
      PlsqlType(
        "something",
        NonEmptyList.of(
          PlsqlField.Number("id"),
          PlsqlField.Text("name"),
          PlsqlField.Text("surname"),
          PlsqlField.Date("born")
        )
      )
    )

    assertEquals(obtained, expected)
  }

  test("should parse sample type with trailing slash") {

    // lack of margin is intentional, to make it even messier
    // more tests should be written in the future
    val input =
      """create or repLace type something as object (
           id number,
           name varchar2 (50 
           )
         ,  species varchar2( 100  )
      );
      /"""

    val obtained = Parser.parse(input)
    val expected = Right(
      PlsqlType(
        "something",
        NonEmptyList.of(
          PlsqlField.Number("id"),
          PlsqlField.Text("name"),
          PlsqlField.Text("species")
        )
      )
    )

    assertEquals(obtained, expected)
  }

  test("should return a nice error message") {
    val input    = "createor" // missing whitespace after create
    val obtained = Parser.parse(input)
    val expected = Left("""createor
                          |      ^
                          |expectations:
                          |* whitespace""".stripMargin)

    assertEquals(obtained, expected)
  }
