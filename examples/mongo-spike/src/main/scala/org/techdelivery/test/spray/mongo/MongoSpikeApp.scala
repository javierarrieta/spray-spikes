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
import spray.util.SprayActorLogging
import akka.event.Logging
import com.typesafe.scalalogging.slf4j.Logging

object MongoSpikeApp extends App with SprayCanHttpServerApp with Logging {
  
  lazy val defaultConf = ConfigFactory.load
  lazy val conf = ConfigFactory.load("mongo-app").withFallback(defaultConf)
  
  logger.debug(defaultConf.root().render())
  logger.debug(conf.root().render())
  
  implicit val ioBridge = IOExtension(system).ioBridge()
  
  implicit val mongo = new MongoDriver
  val connection = mongo.connection(conf.getStringList("mongo.servers").asScala.toSeq)
  
  def messageCreator(ctx:PipelineContext) : ActorRef = {
    system.actorOf(Props( new MongoResource(connection)))
  }
  
  
  val httpServer = system.actorOf(Props(new HttpServer(ioBridge, PerConnectionHandler(messageCreator), ServerSettings())), "http-server")
  // create a new HttpServer using our handler and tell it where to bind to
  httpServer ! Bind(interface = conf.getString("http.server.interface"), port = conf.getInt("http.server.port"))
}