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
    .settings(basicSettings: _*)

  lazy val myfirstspray = Project("my-first-spray", file("examples/my-first-spray"))
    .settings(exampleSettings: _*)
    .settings(libraryDependencies ++=
      compile(akkaActor, sprayCan, sprayRouting, sprayJson) ++
      test(specs2) ++
      provided(akkaSlf4j, logback)
    )

  lazy val httpexample = Project("http-server-example", file("examples/http-server-example"))
    .settings(exampleSettings: _*)
    .settings(libraryDependencies ++=
      compile(akkaActor, sprayCan, sprayJson) ++
      test(specs2) ++
      provided(akkaSlf4j, logback)
    )

  lazy val httpproxy = Project("http-proxy", file("examples/http-proxy"))
    .settings(exampleSettings: _*)
    .settings(libraryDependencies ++=
      compile(akkaActor, sprayCan, sprayJson, sprayClient) ++
      test(specs2) ++
      provided(akkaSlf4j, logback)
   ) 

  lazy val mongospike = Project("mongo-spike", file("examples/mongo-spike"))
    .settings(exampleSettings: _*)
    .settings(libraryDependencies ++=
      compile(akkaActor, sprayCan, sprayJson, rMongo) ++
      test(specs2) ++
      provided(akkaSlf4j, logback)
   ) 

}
