package slicksample

import slick.driver.H2Driver.api._
import slicksample.entity.AuthorEntity
import slicksample.model.Author

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Alexander Kuleshov
 */
object SlickApp extends App {
  val db = Database.forConfig("h2mem1")
  try {

    // The query interface for the Suppliers table
    val authors: TableQuery[AuthorEntity] = TableQuery[AuthorEntity]

    authors.schema.create.statements.foreach(println)

    val setupAction: DBIO[Unit] = DBIO.seq(
      authors.schema.create,

      // Insert some authors
      authors += Author(firstName = "Mark", lastName = "Twain"),
      authors += Author(firstName = "George", lastName = "Martin"),
      authors += Author(firstName = "Jack", lastName = "London")
    )


    val setupFuture: Future[Unit] = db.run(setupAction)
    val f = setupFuture.flatMap { _ =>

      val plainQuery = sql"select * from AUTHOR".as[(Int, String, String)]
      db.run(plainQuery).map(println)

    }. flatMap { _ =>

      val plainQuery = sql"select * from AUTHOR".as[(Int, String, String)]
      db.run(plainQuery).map((v) => v.map((x) => Author(Option(x._1), x._2, x._3))).map(println)

    }
    Await.result(f, Duration.Inf)

    implicit val dba = db
    val f1 = Future {

      AuthorEntity.create(Author(firstName = "Albert", lastName = "Enstein"))
      AuthorEntity.all.map(println)
      AuthorEntity.findById(4).map(println)
      AuthorEntity.count.map(println)
      AuthorEntity.delete(1)
      AuthorEntity.count.map(println)

    }.flatMap { _ =>

      val authorId = 6
      val albert = AuthorEntity.findById(authorId)

      albert.flatMap {
        case Some(author) => {
          val a = author.copy(firstName = "Simon", lastName = "Payton")
          AuthorEntity.update(a)
          AuthorEntity.findById(a.id.get).map(println)
          AuthorEntity.all.map(println)
        }
        case None => {
          println(s"Author with id=$authorId not found")
          Future(0)
        }
      }

    }
    Await.result(f1, Duration.Inf)

  } finally db.close
}