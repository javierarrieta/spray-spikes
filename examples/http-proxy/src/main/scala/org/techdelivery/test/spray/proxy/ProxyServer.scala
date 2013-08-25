package org.techdelivery.test.spray.proxy

import spray.io.PipelineContext
import akka.actor.Props
import java.util.UUID
import spray.can.server.ServerSettings
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration._
import spray.can.Http
import akka.io.IO

object ProxyServer extends App {
    
  implicit val timeout: Timeout = 5 seconds

  implicit val system = ActorSystem()

  val httpServer = system.actorOf(Props[RequestReceiver], "http-server")
  // create a new HttpServer using our handler and tell it where to bind to
  IO(Http) ! Http.Bind(httpServer, interface = "0.0.0.0", port = 9080)
}