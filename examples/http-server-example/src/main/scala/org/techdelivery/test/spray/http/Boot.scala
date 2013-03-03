package org.techdelivery.test.spray.http

import spray.can.server.SprayCanHttpServerApp
import akka.actor.Props

object Boot extends App with SprayCanHttpServerApp {
  
  // create and start our service actor
  val service = system.actorOf(Props[HttpServiceActor], "http-service")

  // create a new HttpServer using our handler and tell it where to bind to
  newHttpServer(service) ! Bind(interface = "localhost", port = 9080)
}