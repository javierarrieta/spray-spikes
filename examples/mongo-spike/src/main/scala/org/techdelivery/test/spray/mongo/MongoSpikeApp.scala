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
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

object MongoSpikeApp extends App with SprayCanHttpServerApp {
  
  val conf = ConfigFactory.load
  
  implicit val ioBridge = IOExtension(system).ioBridge() 
  
  implicit val mongo = new MongoDriver
  val connection = mongo.connection(conf.getStringList("mongo.servers").asScala.toSeq)
  
  def messageCreator(ctx:PipelineContext) : ActorRef = {
    system.actorOf(Props( new MongoResource(connection)))
  }
  
  
  val httpServer = system.actorOf(Props(new HttpServer(ioBridge, PerConnectionHandler(messageCreator), ServerSettings())), "http-server")
  // create a new HttpServer using our handler and tell it where to bind to
  httpServer ! Bind(interface = "0.0.0.0", port = 9080)
}