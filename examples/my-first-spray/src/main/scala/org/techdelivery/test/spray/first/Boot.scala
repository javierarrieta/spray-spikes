package org.techdelivery.test.spray.first

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp
import org.techdelivery.test.spray.first.resource.ExampleServiceActor

object Boot extends App with SprayCanHttpServerApp {
  
  // create and start our service actor
  val service = system.actorOf(Props[ExampleServiceActor], "demo-service")

  // create a new HttpServer using our handler and tell it where to bind to
  newHttpServer(service) ! Bind(interface = "localhost", port = 9080)
}