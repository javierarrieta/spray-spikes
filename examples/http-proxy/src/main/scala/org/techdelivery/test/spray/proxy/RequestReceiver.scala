package org.techdelivery.test.spray.proxy

import akka.actor.Actor
import akka.actor.Props
import spray.can.client.HttpClient
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.util.SprayActorLogging
import akka.actor.ActorRef

class RequestReceiver(client: ActorRef) extends Actor with SprayActorLogging {
  
  def host = "www.google.com"
  val httpClient = client
  def receive = {
    case HttpRequest(method,url,headers,body,_) => {
      log.info(url)
      //httpClient ! Get("http://" + host + url)
      sender ! HttpResponse(200)
    }
    case _ => sender ! HttpResponse(400)
  }
}