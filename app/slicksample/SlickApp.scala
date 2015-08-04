package slicksample

import slick.driver.H2Driver.api._
import slicksample.entity.{AuthorBookTable, BookEntity, PublisherEntity, AuthorEntity}
import slicksample.model.{Book, Publisher, Author}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Alexander Kuleshov
 */
object SlickApp extends App {
  implicit val db = Database.forConfig("h2mem1")
  try {

    AuthorEntity.authors.schema.create.statements.foreach(println)
    PublisherEntity.publishers.schema.create.statements.foreach(println)
    BookEntity.books.schema.create.statements.foreach(println)
    AuthorBookTable.authorBooks.schema.create.statements.foreach(println)

    val setupAction: DBIO[Unit] = DBIO.seq(
      AuthorEntity.authors.schema.create,
      PublisherEntity.publishers.schema.create,
      BookEntity.books.schema.create,
      AuthorBookTable.authorBooks.schema.create,

      // Insert some authors
      AuthorEntity.authors += Author(firstName = "Mark", lastName = "Twain"),
      AuthorEntity.authors += Author(firstName = "George", lastName = "Martin"),
      AuthorEntity.authors += Author(firstName = "Jack", lastName = "London")
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

    val f1 = Future {

      AuthorEntity.create(Author(firstName = "Albert", lastName = "Enstein"))
      AuthorEntity.all.map(println)
      AuthorEntity.findById(4).map(println)
      AuthorEntity.count.map(println)
      AuthorEntity.delete(1)
      AuthorEntity.count.map(println)

    }.flatMap { _ =>

      val authorId = 4
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

    val f2 = Future {
      PublisherEntity.create(Publisher(name = "O'Reily"))
      PublisherEntity.all().map(println)

    }. flatMap { _ =>

      val publisherId = 1

      val oreily = PublisherEntity.findById(publisherId)

      oreily.flatMap {
        case Some(publisher) => {
          BookEntity.create(Book(title = "Scala: All about", publisherId = publisher.id))
          val book = BookEntity.findById(1)
          book.map { b =>
            println("Book: " + b.get)
            b
          }
          PublisherEntity.delete(publisherId)
          PublisherEntity.all().map(println)
          BookEntity.all().map(println)
        }
        case None => {
          println(s"Publisher with id=$publisherId not found")
          Future(0)
        }
      }
    }
    Await.result(f2, Duration.Inf)


    val profile: slick.driver.JdbcProfile = slick.driver.H2Driver

    import profile.simple._

    db.withSession { implicit session =>
      val q = for {
        b <- BookEntity.books
        p <- b.publisher
        if b.id === 1
      } yield (p.name, b.title)

      println(q.selectStatement)
      println(q.run)

    }

    val f4 = Future {
      BookEntity.findTupleById(3).map(println)
      BookEntity.findTupleById(1).map(println)
    }
    Await.result(f4, Duration.Inf)

    val f5 = Future {

      val bookF = BookEntity.findById(1)
      val authorF1 = AuthorEntity.findById(2)
      val authorF2 = AuthorEntity.findById(3)
      val authorF3 = AuthorEntity.findById(4)

      val future = for {
        book <- bookF
        author1 <- authorF1
        author2 <- authorF2
        author3 <- authorF3
      } yield ((book.get, List(author1.get, author2.get, author3.get)))

      future.flatMap { tuple =>

        for (author <- tuple._2) {
          AuthorBookTable.create(author.id.get, tuple._1.id.get)
        }
//        AuthorBookTable.all.map(println)
//        AuthorBookTable.findTupleByBookId(1).map(println)
        AuthorBookTable.findTupleByBookId3(1).map(println)
        Future(1)
      }
    } andThen {
        case _ =>

      //      AuthorBookTable.findTupleByBookId(1).map(println)
            AuthorBookTable.findTupleByBookId2(1).map(println)
        AuthorBookTable.all.map(println)
    }
    Await.result(f5, Duration.Inf)


  } finally db.close
}