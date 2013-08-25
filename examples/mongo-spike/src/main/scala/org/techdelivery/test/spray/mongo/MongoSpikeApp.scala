package org.techdelivery.test.spray.mongo

import akka.actor.{ActorSystem, Props, ActorRef}
import spray.io.PipelineContext
import reactivemongo.api.MongoDriver
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._
import com.typesafe.scalalogging.slf4j.Logging
import spray.can.Http
import akka.io.IO

object MongoSpikeApp extends App with Logging {

  implicit val system = ActorSystem()
  
  lazy val defaultConf = ConfigFactory.load
  lazy val conf = ConfigFactory.load("mongo-app").withFallback(defaultConf)
  
  logger.debug(defaultConf.root().render())
  logger.debug(conf.root().render())
  
  implicit val mongo = new MongoDriver
  val connection = mongo.connection(conf.getStringList("mongo.servers").asScala.toSeq)
  
  def messageCreator(ctx:PipelineContext) : ActorRef = {
    system.actorOf(Props( new MongoResource(connection)))
  }
  
  
  val httpServer = system.actorOf(Props[MongoResource], "http-server")
  // create a new HttpServer using our handler and tell it where to bind to
  IO(Http) ! Http.Bind(httpServer, interface = conf.getString("http.server.interface"), port = conf.getInt("http.server.port"))
}