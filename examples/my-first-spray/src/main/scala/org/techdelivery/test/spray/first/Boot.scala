package org.techdelivery.test.spray.first

import akka.actor.{ActorSystem, Props}
import org.techdelivery.test.spray.first.resource.ExampleServiceActor
import spray.can.Http
import akka.io.IO

object Boot extends App {

  implicit val system = ActorSystem()
  
  // create and start our service actor
  val service = system.actorOf(Props[ExampleServiceActor], "demo-service")

  // create a new HttpServer using our handler and tell it where to bind to
  IO(Http) ! Http.Bind( service, interface = "localhost", port = 9080)
}