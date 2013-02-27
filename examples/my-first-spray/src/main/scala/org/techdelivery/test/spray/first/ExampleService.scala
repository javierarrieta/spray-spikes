package org.techdelivery.test.spray.first

import akka.actor.Actor
import spray.routing.HttpService
import spray.http.HttpResponse
import spray.http.HttpHeaders.RawHeader

class ExampleServiceActor extends Actor with ExampleService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(route)
  
}

trait ExampleService extends HttpService {
  val route = {
    get {
      path("ping") {
        complete(HttpResponse(201,"PONG!",List(RawHeader("a","b"))))
      }
    }
  }
}

