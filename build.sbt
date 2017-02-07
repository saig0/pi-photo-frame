organization := "de"

name := "pi-photo-frame"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.102-R11"
 
libraryDependencies ++= List(
	"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
	"junit" % "junit" % "4.11" % "test"
)