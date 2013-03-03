package org.techdelivery.test.spray.http

import akka.actor.Actor
import spray.util.SprayActorLogging
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.HttpMethods._
import akka.actor.actorRef2Scala
import spray.http.HttpEntity.apply

class HttpServiceActor extends Actor with SprayActorLogging {

  def receive = {
    case HttpRequest(GET, "/ping", _, _, _) =>
      sender ! HttpResponse(entity = "pong")
      
    case HttpRequest(POST, "/file",headers,body,_) =>
      val fileBody = body
      val fileHeaders = headers
      sender ! HttpResponse( status = 201)

    case _: HttpRequest => sender ! HttpResponse(status = 404, entity = "Unknown resource!")
  }
}
