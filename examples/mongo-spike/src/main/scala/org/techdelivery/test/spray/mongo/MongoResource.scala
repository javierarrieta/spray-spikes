package org.techdelivery.test.spray.mongo

import spray.util.SprayActorLogging
import akka.actor.Actor
import spray.http.{HttpBody, HttpRequest, HttpResponse}
import spray.http.HttpHeaders._
import spray.http.HttpMethods._
import spray.http.MediaTypes._
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONDocument
import scala.util.Success
import scala.util.Failure
import reactivemongo.api.MongoConnection
import org.techdelivery.test.spray.mongo.entity.{NewPerson, PersonProtocol, Person}
import org.techdelivery.test.spray.mongo.entity.mappings._
import org.techdelivery.test.spray.mongo.entity.PersonProtocol._
import spray.json._
import DefaultJsonProtocol._

class MongoResource(connection: MongoConnection) extends Actor with SprayActorLogging {

  val db = connection.db("people")
  val collection = db.collection("person")
  val jsonHeaders = List(`Content-Type`(`application/json`))
  
  def receive = {
    case HttpRequest(GET,"/person",_,_,_) => {
      val origin = sender
      val filter = BSONDocument()
      val cursor = collection.find(filter).cursor[Person]
      val response = cursor.toList
      response onComplete {
        case Success(list) => {
          origin ! HttpResponse( status = 200, entity = HttpBody(`application/json`, list.toJson.toString()))
        }
        case Failure(t) => origin ! HttpResponse( status = 500, entity = t.getLocalizedMessage())
      }
    }

    case HttpRequest(POST,"/person",headers,entity,_) => {
      val origin = sender
      val person = entity.asString.asJson.convertTo[NewPerson]
      val fResult = collection.insert(person)
      fResult onComplete {
        case Success(o) => {
          origin ! HttpResponse( status = 201 )
        }
        case Failure(t) => origin ! HttpResponse( status = 500, entity = t.getLocalizedMessage())
      }
    }
    
    case HttpRequest(_,_,_,_,_) => sender ! HttpResponse( status = 404, entity = "")
      
  }
}