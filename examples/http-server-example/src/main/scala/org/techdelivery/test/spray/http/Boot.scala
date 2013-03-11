package org.techdelivery.test.spray.http

import spray.can.server.SprayCanHttpServerApp
import akka.actor.Props
import spray.can.server.HttpServer
import spray.io.PerConnectionHandler
import spray.io.IOExtension
import spray.io.PipelineContext
import akka.actor.ActorRef
import spray.io.PerConnectionHandler
import spray.can.server.ServerSettings
import java.util.UUID

object Boot extends App with SprayCanHttpServerApp {
  
  def messageCreator(ctx:PipelineContext) : ActorRef = {
    val actorId: String = UUID.randomUUID().toString()
    system.actorOf(Props[HttpServiceActor], actorId)
  }
  
  val ioBridge = IOExtension(system).ioBridge()
  val httpServer = system.actorOf(Props(new HttpServer(ioBridge, PerConnectionHandler(messageCreator), ServerSettings())), "http-server")
  // create a new HttpServer using our handler and tell it where to bind to
  httpServer ! Bind(interface = "0.0.0.0", port = 9080)
}