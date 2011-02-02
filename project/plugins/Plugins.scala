import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val eclipse = "de.element34" % "sbt-eclipsify" % "0.7.0"
  val akkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "1.0-RC3"
}
