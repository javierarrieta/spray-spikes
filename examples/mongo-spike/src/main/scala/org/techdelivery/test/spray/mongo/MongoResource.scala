package org.techdelivery.test.spray.mongo

import spray.util.SprayActorLogging
import akka.actor.Actor
import spray.http.HttpRequest
import spray.http.HttpMethods._
import spray.http.MediaTypes._
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONDocument
import spray.http.HttpResponse
import scala.util.Success
import scala.util.Failure
import reactivemongo.api.MongoConnection
import org.techdelivery.test.spray.mongo.entity.{PersonProtocol, Person}
import org.techdelivery.test.spray.mongo.entity.mappings._
import org.techdelivery.test.spray.mongo.entity.PersonProtocol._
import spray.json._
import DefaultJsonProtocol._

class MongoResource(connection: MongoConnection) extends Actor with SprayActorLogging {

  val db = connection.db("people")
  
  def receive = {
    case HttpRequest(GET,"/",_,_,_) => {
      val origin = sender
      val collection = db.collection("person")
      val filter = BSONDocument()
      val fields = BSONDocument("first_name" -> 1, "last_name" -> 1)
      val cursor = collection.find(filter).cursor[Person]
      val response = cursor.toList
      response onComplete {
        case Success(list) => {
          origin ! HttpResponse( status = 200, entity = list.toJson.toString())
        }
        case Failure(t) => origin ! HttpResponse( status = 500, entity = t.getLocalizedMessage())
      }
    }
    
    case HttpRequest(_,_,_,_,_) => sender ! HttpResponse( status = 404, entity = "")
      
  }
}