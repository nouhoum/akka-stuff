import sbt._
import de.element34.sbteclipsify._


class AkkaArticle(info: ProjectInfo) extends DefaultProject(info) with Eclipsify with AkkaProject {
   val akkaRemote = akkaModule("remote")
   val akkaStm = akkaModule("stm")
}
