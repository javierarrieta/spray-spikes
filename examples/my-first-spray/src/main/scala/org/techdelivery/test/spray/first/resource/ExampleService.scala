package org.techdelivery.test.spray.first.resource

import akka.actor.Actor
import spray.routing.HttpService
import spray.http.HttpResponse
import spray.http.HttpHeaders.RawHeader
import spray.http.HttpEntity.apply
import spray.http.StatusCode.int2StatusCode
import spray.routing.Directive.pimpApply
import spray.routing.directives.CompletionMagnet.fromHttpResponse
import org.techdelivery.test.spray.first.model.Person
import org.techdelivery.test.spray.first.marshal.ExampleMarshaller._

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
        complete(HttpResponse(200,"PONG!",List(RawHeader("a","b"))))
      }
    } ~
    path("person"/LongNumber) {id =>
      get {
        val person: Person = Person(id,"Name","Surname")
        complete(person)
      }
    }
  }
}

