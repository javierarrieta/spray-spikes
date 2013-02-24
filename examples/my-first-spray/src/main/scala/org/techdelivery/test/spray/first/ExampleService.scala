package org.techdelivery.test.spray.first

import akka.actor.Actor
import spray.routing.HttpService

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
        complete("PONG!")
      }
    }
  }
}

