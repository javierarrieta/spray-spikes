package org.techdelivery.test.spray.http

import akka.actor.{ActorLogging, Actor, actorRef2Scala}
import spray.http._
import spray.http.HttpMethods._
import spray.http.HttpEntity.apply
import java.util.UUID
import java.io.File
import java.io.FileOutputStream
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.ChunkedRequestStart

class HttpServiceActor extends Actor with ActorLogging {
  val uuid = UUID.randomUUID().toString()
  val f: File = new File("/tmp",uuid + ".tmp")
  val fs = new FileOutputStream(f)
  def receive = {
    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
      sender ! HttpResponse(entity = "pong")
      
    case HttpRequest(POST, Uri.Path("/file"),headers,body,_) =>
      log.info( "File uploaded " + body.buffer.length + " bytes :"+ uuid )

      sender ! HttpResponse( status = 201)
      
    case ChunkedRequestStart(request) =>
      //log.info( "Chunked request start[" + uuid + "], headers: " + request.headers )
      val ref = sender
      log.info("Starting write to file " + f.getCanonicalPath() )
      
    case MessageChunk(body,extensions) =>
      log.info( "Chunk received: " + body.length + " bytes, to file: " + f.getCanonicalPath() )
      val ref = sender
      fs.write(body.buffer)
//      Thread.sleep(20)
//      sender ! HttpResponse(status=100)
    
    case ChunkedMessageEnd(extensions, trailer) =>
      log.info( "Chunked request end, file: " + f.getCanonicalPath() )
      fs.close()      
      sender ! HttpResponse(status = 201)
      
//    case Timeout(request) =>
//      sender ! HttpResponse(status = 502, entity = "Timeout processing request: " + uuid)
      
    case _: HttpRequest => 
      sender ! HttpResponse(status = 404, entity = "Unknown resource!")
  }
}
