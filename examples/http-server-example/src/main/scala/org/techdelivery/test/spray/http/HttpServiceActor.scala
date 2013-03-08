package org.techdelivery.test.spray.http

import akka.actor.Actor
import spray.util.SprayActorLogging
import spray.http.HttpRequest
import spray.http.HttpMethods._
import akka.actor.actorRef2Scala
import spray.http.HttpEntity.apply
import spray.http.ChunkedRequestStart
import spray.http.MessageChunk
import spray.http.ChunkedMessageEnd
import spray.http.HttpResponse
import java.util.UUID

class HttpServiceActor extends Actor with SprayActorLogging {
  val uuid:String = UUID.randomUUID().toString
  def receive = {
    case HttpRequest(GET, "/ping", _, _, _) =>
      sender ! HttpResponse(entity = "pong")
      
    case HttpRequest(POST, "/file",headers,body,_) =>
      log.info( "File uploaded " + body.buffer.length + " bytes :"+ headers )

      sender ! HttpResponse( status = 201)
      
    case ChunkedRequestStart(request) =>
      log.info( "Chunked request start, headers: " + request.headers )
      
    case MessageChunk(body,extensions) =>
      log.info( "Chunk received: " + body.length + " bytes, extensions: " + extensions )
    
    case ChunkedMessageEnd(extensions, trailer) =>
      log.info( "Chunked request end, headers: " + trailer )
      
      sender ! HttpResponse(status = 201)
      
    case _: HttpRequest => sender ! HttpResponse(status = 404, entity = "Unknown resource!")
  }
}
