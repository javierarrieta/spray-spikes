package org.techdelivery.test.spray.mongo.entity

import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONString
import reactivemongo.bson.BSONString
import reactivemongo.bson.Producer.nameValue2Producer
import reactivemongo.bson.BSONDocumentReader
import spray.json.DefaultJsonProtocol

case class Person(id: String, first_name: String, last_name: String)

object mappings {

	implicit object PersonMapper extends BSONDocumentWriter[Person] with BSONDocumentReader[Person] {
	  def write(person: Person) : BSONDocument = BSONDocument(
	    "_id" -> BSONObjectID(person.id),
	    "first_name" -> BSONString(person.first_name),
	    "last_name" -> BSONString(person.last_name)
	  )
	  def read(doc: BSONDocument) : Person = {
	    new Person(
	      doc.get("_id").get.toString(),
	      doc.get("first_name").get.toString(),
	      doc.get("last_name").get.toString()
	    )
	  }
	}
}

object PersonProtocol extends DefaultJsonProtocol {
  implicit val personFormat = jsonFormat3(Person)
}