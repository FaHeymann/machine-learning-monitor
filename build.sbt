name := """machine-learning-monitor"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Error)

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "mysql" % "mysql-connector-java" % "5.1.36",
  "org.mindrot" % "jbcrypt" % "0.3m",

  // assets
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "org.webjars" % "font-awesome" % "4.6.1",
  "org.webjars" % "angularjs" % "1.5.0",
  "org.webjars.npm" % "angular-chart.js" % "1.0.2",
  "org.webjars" % "angular-ui-bootstrap" % "1.2.1"
)
