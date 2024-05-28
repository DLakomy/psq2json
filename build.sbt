ThisBuild / version      := "0.1.0"
ThisBuild / scalaVersion := "3.3.3"
ThisBuild / organization := "io.github.dlakomy"

ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Wunused:all"
)


val testContainersVersion = "1.19.8"

lazy val converter = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/converter"))
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel"           %% "cats-parse"     % "1.0.0",
      "org.testcontainers"       % "testcontainers" % testContainersVersion % Test,
      "org.testcontainers"       % "oracle-xe"      % testContainersVersion % Test,
      "org.scalameta"           %% "munit"          % "1.0.0"               % Test,
      "com.oracle.database.jdbc" % "ojdbc11"        % "23.4.0.24.05"        % Test,
      "io.circe"                %% "circe-parser"   % "0.14.7"              % Test
    )
  )


lazy val app = (project in file("modules/app"))
  .settings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(converter.js)


lazy val root = project
  .in(file("."))
  .settings(
    name    := "psq2json",
    version := "0.1.0-SNAPSHOT"
  )
  .aggregate(converter.jvm, app)
  .dependsOn(converter.jvm, app)
