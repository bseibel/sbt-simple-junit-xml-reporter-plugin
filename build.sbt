sbtPlugin := true

name := "sbt-simple-junit-xml-reporter-plugin"

organization := "ca.seibelnet"

version := "0.3"

resolvers ++= Seq(
        "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
    "org.mockito"           %   "mockito-all"           % "1.9.5"     % "test",
    "org.scalatest"         %   "scalatest_2.10"        % "2.0"       % "test"
)
