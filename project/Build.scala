import sbt._
import Keys._


object Build extends Build {
  import BuildSettings._
  import Dependencies._

  // configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Root Project
  // -------------------------------------------------------------------------------------------------------------------

  lazy val root = Project("root",file("."))
    .aggregate(examples)
    .settings(basicSettings: _*)



  // -------------------------------------------------------------------------------------------------------------------
  // Example Projects
  // -------------------------------------------------------------------------------------------------------------------

  lazy val examples = Project("examples", file("examples"))
    .aggregate(myfirstspray)
    .settings(settings: _*)

  lazy val myfirstspray = Project("my-first-spray", file("examples/my-first-spray"))
    .settings(exampleSettings: _*)
    .settings(libraryDependencies ++=
      compile(akkaActor,sprayCan) ++
      test(specs2) ++
      runtime(akkaSlf4j, logback)
    )

}
