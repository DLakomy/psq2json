package io.github.dlakomy.psq2json

import io.circe.Json
import io.circe.parser.parse

import fixtures.TestCases


class ConversionFlowSuite extends OracleSharedContainerSuite:

  TestCases.foreach: testCase =>
    test(testCase.name):
      // prepare
      val conn = getSharedConnection()
      conn.executeStatement(testCase.createTypeSql)

      // code under test - create the function
      val createFn = Converter.convert(testCase.createTypeSql) match
        case Left(error)     => fail(s"Conversion failed with error:\n$error")
        case Right(createFn) => createFn

      conn.executeStatement(createFn)

      // checking the result
      val result = parse(conn.getClobFromFnCall(testCase.callFnSql)) match
        case Left(error)   => fail(s"Output parsing failed with error:\n$error")
        case Right(result) => result

      assertEquals(clue(result), testCase.resultJson)
