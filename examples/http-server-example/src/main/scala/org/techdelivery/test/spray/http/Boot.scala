package org.techdelivery.test.spray.http

import akka.actor.{ActorSystem, Props, ActorRef}
import spray.io.PipelineContext
import java.util.UUID
import spray.can.Http
import akka.io.IO

object Boot extends App {

  implicit val system = ActorSystem()
  
  def messageCreator(ctx:PipelineContext) : ActorRef = {
    val actorId: String = UUID.randomUUID().toString()
    system.actorOf(Props[HttpServiceActor], actorId)
  }

  val httpServer = system.actorOf(Props[HttpServiceActor], "http-server")
  // create a new HttpServer using our handler and tell it where to bind to
  IO(Http) ! Http.Bind(httpServer, interface = "0.0.0.0", port = 9080)
}