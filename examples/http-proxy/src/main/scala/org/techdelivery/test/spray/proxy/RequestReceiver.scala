package org.techdelivery.test.spray.proxy

import akka.actor.Actor
import akka.actor.Props
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.util.SprayActorLogging
import akka.actor.ActorRef
import spray.can.client.HttpClient._
import spray.can.client.HttpDialog
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import scala.util.Try

class RequestReceiver(client: ActorRef) extends Actor with SprayActorLogging {

  def host = "www.google.com"
  def port = 80
  val httpClient = client
  def receive = {
    case request: HttpRequest => {
      log.info(request.uri)
      fetchRequest(request, sender)
    }
    case _ => { request: Unit =>
      log.warning("Unknown request: ", request)
      sender ! HttpResponse(400)
    }
  }

  def fetchRequest(request: HttpRequest, sender: ActorRef): Unit = {
    val dialog = HttpDialog(httpClient, host, port)
    dialog.send(request).end.onComplete { result =>
      result match {
        case Success(response) => {
          sender ! response
        }
        case Failure(error) => {
          log.info("Error from downstream server: ", error)
          sender ! HttpResponse(503)
        }
      }
    }
  }
}