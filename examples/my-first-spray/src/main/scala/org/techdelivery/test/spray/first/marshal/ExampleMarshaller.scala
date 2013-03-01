package org.techdelivery.test.spray.first.marshal

import spray.json.DefaultJsonProtocol
import org.techdelivery.test.spray.first.model.Person
import spray.httpx.SprayJsonSupport

object ExampleMarshaller extends DefaultJsonProtocol with SprayJsonSupport {
	implicit val personFormat = jsonFormat3(Person)
}