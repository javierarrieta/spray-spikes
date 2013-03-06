package org.techdelivery.test.spray.http

import akka.actor.Actor
import spray.util.SprayActorLogging
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.HttpMethods._
import akka.actor.actorRef2Scala
import spray.http.HttpEntity.apply
import spray.http.ChunkedRequestStart
import spray.http.MessageChunk
import spray.http.ChunkedMessageEnd
import spray.http.HttpResponse

class HttpServiceActor extends Actor with SprayActorLogging {

  def receive = {
    case HttpRequest(GET, "/ping", _, _, _) =>
      sender ! HttpResponse(entity = "pong")
      
    case HttpRequest(POST, "/file",headers,body,_) =>
      val fileBody = body
      val fileHeaders = headers
      sender ! HttpResponse( status = 201)
      
    case ChunkedRequestStart(request) =>
      val req = request
      
    case MessageChunk(body,_) =>
      val entity = body
    
    case ChunkedMessageEnd(extensions, trailer) =>
      val ext = extensions
      val headers = trailer
      
      sender ! HttpResponse(status = 201)
      
    case _: HttpRequest => sender ! HttpResponse(status = 404, entity = "Unknown resource!")
  }
}
