name := """testeJDK11"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = ( project in file(".") )
  .enablePlugins(PlayJava)


scalaVersion := "2.13.0"

// Play: Guice
libraryDependencies += guice

// Play: JPA
libraryDependencies += javaJpa

// SQLite
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.28.0"
libraryDependencies += "com.zsoltfabok" % "sqlite-dialect" % "1.0"

// Hibernate
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.4.7.Final"

// Vavr.
libraryDependencies += "io.vavr" % "vavr" % "0.10.2"

// Apache Commons.
libraryDependencies += "commons-lang" % "commons-lang" % "2.6"


routesImport += "infra.routes.LongBinder"