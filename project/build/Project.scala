import sbt._
import de.element34.sbteclipsify._

class Project(info: ProjectInfo) extends DefaultProject(info) with Eclipsify with AkkaProject {
	val akkaRemote = akkaModule("remote")
}
