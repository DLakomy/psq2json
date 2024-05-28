package io.github.dlakomy.psq2json

import munit.Fixture

import java.sql.{Connection, DriverManager, Types}


/** provides a container with one connection shared among all tests (no cleanup inbetween is done)
  */
trait OracleSharedContainerSuite extends munit.FunSuite:

  protected val getSharedConnection = new Fixture[Connection]("database"):
    var connection: Connection = null
    def apply()                = connection

    override def beforeAll(): Unit =
      val connParams =
        if sys.env.getOrElse("PSQ2JSON_TEST_TC_REUSABLE", "") == "1" then "?TC_REUSABLE=true"
        else ""

      // doesn't find the driver without this ¯\_(ツ)_/¯
      DriverManager.getDrivers()
      connection = DriverManager.getConnection(
        "jdbc:tc:oracle:21-slim-faststart:///testdb" + connParams,
        "oracle",
        "oracle"
      )

    override def afterAll(): Unit =
      connection.close()

  override def munitFixtures = List(getSharedConnection)

  extension (conn: Connection)
    /** @param fnCall
      *   Example (no binds allowed, must return clob): animal_to_json(animal(1,'John','Dove'))
      */
    def getClobFromFnCall(fnCall: String): String =
      val stmt = conn.prepareCall(s"{ ? = call $fnCall }")
      stmt.registerOutParameter(1, Types.CLOB)
      stmt.execute()
      val clob = stmt.getClob(1)
      clob.getSubString(1, clob.length.toInt)

    def executeStatement(statement: String): Unit =
      conn.createStatement().execute(statement)
