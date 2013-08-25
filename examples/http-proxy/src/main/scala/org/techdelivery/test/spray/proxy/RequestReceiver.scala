package org.techdelivery.test.spray.proxy

import akka.actor.{ActorLogging, Actor, ActorRef}
import spray.http.HttpRequest
import spray.http.HttpResponse
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import spray.client.HttpDialog
import spray.can.Http

class RequestReceiver extends Actor with ActorLogging {

  def host = "www.google.com"
  def port = 80
  val conn = Http.Connect(host, port)
  def receive = {
    case request: HttpRequest => {
      log.info(request.uri.toString())
      fetchRequest(request, sender)
    }
    case _ => { request: Unit =>
      log.warning("Unknown request: ", request)
      sender ! HttpResponse(400)
    }
  }

  def fetchRequest(request: HttpRequest, sender: ActorRef): Unit = {
    val dialog = HttpDialog(conn)
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