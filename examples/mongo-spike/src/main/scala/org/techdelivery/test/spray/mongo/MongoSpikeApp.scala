package org.techdelivery.test.spray.mongo

import spray.can.server.SprayCanHttpServerApp
import akka.actor.Props
import spray.io.PipelineContext
import akka.actor.ActorRef
import spray.can.server.HttpServer
import spray.io.IOExtension
import spray.io.PerConnectionHandler
import spray.can.server.ServerSettings
import reactivemongo.api.MongoDriver

object MongoSpikeApp extends App with SprayCanHttpServerApp {
  
  implicit val ioBridge = IOExtension(system).ioBridge() 
  
  implicit val mongo = new MongoDriver
  val connection = mongo.connection(List("192.168.0.68"))
  
  def messageCreator(ctx:PipelineContext) : ActorRef = {
    system.actorOf(Props( new MongoResource(connection)))
  }
  
  
  val httpServer = system.actorOf(Props(new HttpServer(ioBridge, PerConnectionHandler(messageCreator), ServerSettings())), "http-server")
  // create a new HttpServer using our handler and tell it where to bind to
  httpServer ! Bind(interface = "0.0.0.0", port = 9080)
}