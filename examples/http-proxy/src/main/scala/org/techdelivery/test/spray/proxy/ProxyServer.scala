package org.techdelivery.test.spray.proxy

import spray.can.server.SprayCanHttpServerApp
import spray.can.client.HttpClient
import spray.io.IOExtension
import spray.io.PipelineContext
import akka.actor.Props
import java.util.UUID
import spray.can.server.HttpServer
import spray.io.PerConnectionHandler
import spray.can.server.ServerSettings
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration._

object ProxyServer extends App with SprayCanHttpServerApp {
    
  implicit val timeout: Timeout = 5 seconds span
  
  def messageCreator(ctx:PipelineContext) : ActorRef = {
    val actorId: String = UUID.randomUUID().toString()
    val p = Props(new RequestReceiver(client))
    system.actorOf(p, actorId)
  }
  
  implicit val ioBridge = IOExtension(system).ioBridge()
  val client = system.actorOf(Props( new HttpClient(ioBridge) ), "client-actor")
  val httpServer = system.actorOf(Props(new HttpServer(ioBridge, PerConnectionHandler(messageCreator), ServerSettings())), "http-server")
  // create a new HttpServer using our handler and tell it where to bind to
  httpServer ! Bind(interface = "0.0.0.0", port = 9080)
}