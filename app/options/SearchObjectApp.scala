package options

import scala.util.{Failure, Success, Random, Try}

/**
 * @author Alexander Kuleshov
 */
object SearchObjectApp extends App {

  println("/* ---------- Step 1 ---------- */")

  def searchObject(id: String)(implicit service: ObjectService): MyObject = service.getObject(id)

  implicit object ObjectServiceImpl extends ObjectService {
    override def getObject(id: String): MyObject = MyObject(id)
  }

  val obj = searchObject("id1")
  val response = s"(200, ${obj.toString()})"
  println(response)

  println("\n/* ---------- Step 2 ---------- */")

  def searchObject2(id: String)(implicit service: ObjectService2): Option[MyObject] = service.getObject(id)

  implicit object ObjectServiceImpl2 extends ObjectService2 {
    def getObject(id: String): Option[MyObject] = if (Random.nextInt(2) == 0) Some(MyObject(id)) else None
  }

  (for(i <- 1 to 10) yield searchObject2("id2")).map(obj => s"(200, ${obj.toString})").foreach(println)

  println("\n/* ---------- Step 3 ---------- */")

  def searchObject3(id: String)(implicit service: ObjectService3): Try[Option[MyObject]] = Try(service.getObject(id))

  implicit object ObjectServiceImpl3 extends ObjectService3 {
    def getObject(id: String): Option[MyObject] = (Random.nextInt(2), Random.nextInt(2)) match {
      case (0, 0) => Some(MyObject(id))
      case (0, 1) => None
      case (1, _) => throw new Exception("Something bad happened")
    }
  }

  (for(i <- 1 to 10) yield searchObject3("id3")).map( obj => obj match {
    case Success(obj) => s"(200, ${obj.toString})"
    case Failure(err) => s"(500, ${err.getMessage})"
  }).foreach(println)

  println("\n/* ---------- Step 4 ---------- */")

  def searchObject4(id: String)(implicit service: ObjectService3): Either[Try[Option[MyObject]], InternalError] = {
    if (!id.startsWith("id")) Right(InternalError(1000)) else Left(Try(service.getObject(id)))
  }

  val ids = for(i <- 1 to 20) yield if(Random.nextInt(2) == 0) s"id$i" else "invalid"

  ids.map(searchObject4).map(obj => obj match {
    case Left(Failure(err)) => s"(500, ${err.getMessage})"
    case Left(Success(Some(obj))) => s"(200, ${obj.toString})"
    case Left(Success(None)) => s"(404, )"
    case Right(InternalError(1000)) => s"(400, Input id must start with 'id')"
    case Right(InternalError(_)) => s"(400, Better check your input)"
  }).foreach(println)

}

trait ObjectService {
  def getObject(id: String): MyObject
}

trait ObjectService2 {
  def getObject(id: String): Option[MyObject]
}

trait ObjectService3 {
  def getObject(id: String): Option[MyObject]
}

case class InternalError(id: Int)

case class MyObject(id: String) {
  override def toString = { s"""{"id": $id}""" }
}