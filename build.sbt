organization := "de"

name := "pi-photo-frame"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
libraryDependencies ++= List(
	"org.scalafx" %% "scalafx" % "8.0.102-R11",
	"org.scalafx" %% "scalafxml-core-sfx8" % "0.3",
	"net.liftweb" %% "lift-json" % "2.6.2",
	"net.liftweb" %% "lift-json-ext" % "2.6.2",
	"com.drewnoakes" % "metadata-extractor" % "2.10.1"
)
 
libraryDependencies ++= List(
	"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
	"junit" % "junit" % "4.11" % "test"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)