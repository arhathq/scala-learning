package slicksample.entity

import slick.driver.H2Driver.api._
import slicksample.model.{Author, Book, Publisher}

import scala.collection.mutable.ArrayBuffer
import scala.collection.{Seq, mutable}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Alexander Kuleshov
 */
class AuthorBookTable(tag: Tag) extends Table[(Int, Int)](tag, "AUTHOR_BOOK") {
  def authorId = column[Int]("AUTHOR_ID")
  def bookId = column[Int]("BOOK_ID")

  override def * = (authorId, bookId)

  def author = foreignKey("AUTHOR_FK", authorId, AuthorEntity.authors)(_.id, onDelete = ForeignKeyAction.Cascade)
  def book = foreignKey("BOOK_FK", bookId, BookEntity.books)(_.id, onDelete = ForeignKeyAction.Cascade)
}

object AuthorBookTable {

  val authorBooks = TableQuery[AuthorBookTable]

  def create(authorId: Int, bookId: Int)(implicit db: Database) = {
    (authorBooks +=(authorId, bookId)).statements.foreach(println)
    db.run(authorBooks +=(authorId, bookId))
  }

  def all()(implicit db: Database): Future[Seq[(Int, Int)]] = {
    authorBooks.result.statements.foreach(println)
    db.run(authorBooks.result)
  }

  def findTupleByBookId(bookId: Int)(implicit db: Database): Future[Seq[(Book, Author)]] = {

    val q = for {
      ab <- AuthorBookTable.authorBooks
      b <- ab.book
      a <- ab.author
      if (b.id === bookId) && (b.id === ab.bookId)
    } yield (b, a)

    q.result.statements.foreach(println)

    db.run(q.result)
  }

  def findTupleByBookId2(bookId: Int)(implicit db: Database): Future[(Book, List[Author])] = {

    val q = for {
      ab <- AuthorBookTable.authorBooks
      b <- ab.book
      a <- ab.author
      if (b.id === bookId) && (b.id === ab.bookId)
    } yield (b, a)

    q.result.statements.foreach(println)

    db.run(q.result).collect { case seq =>
      val authors: ArrayBuffer[Author] = ArrayBuffer()
      var book: Book = null
      for (row <- seq) {
        book = row._1
        authors += row._2
      }
      (book, authors.toList)
    }
  }

  def findTupleByBookId3(bookId: Int)(implicit db: Database): Future[List[(Book, Publisher, mutable.Set[Author])]] = {
    val q = BookEntity.books.filter(b => b.id === bookId).flatMap { b =>
      AuthorBookTable.authorBooks.filter(ab => ab.bookId === b.id).flatMap { ab =>
        AuthorEntity.authors.filter(a => a.id === ab.authorId).flatMap { a =>
          PublisherEntity.publishers.filter(p => p.id === b.publisherId).map { p =>
            ((b.id, b.title), (p.id, p.name), (a.id, a.firstName, a.lastName))
          }
        }
      }
    }

    q.result.statements.foreach(println)

    db.run(q.result).map( seq => {

      val m: mutable.Map[Any, (Book, Publisher, mutable.Set[Author])] = mutable.Map()

      for (row <- seq) {
        row match {
          case ((bid, t), (pid, n), (aid, fn, ln)) => {
            val r = m.getOrElse(bid, (Book(Some(bid), t), Publisher(Some(pid), n), mutable.Set[Author]()))
            r._3 += Author(Some(aid), fn, ln)
            m += (bid -> r)
          }
        }
      }
      m.values.toList

    })

//    Future((Book(title = "Hand Book"), List()))
//    Future(List((Book(title = "Hand Book"), Publisher(name = "None"), Author(firstName = "", lastName = ""))))
  }
}