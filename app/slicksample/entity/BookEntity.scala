package slicksample.entity

import slick.driver.H2Driver.api._
import slicksample.model.{Publisher, Book}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Alexander Kuleshov
 */
class BookEntity(tag: Tag) extends Table[Book](tag, "BOOK") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def title = column[String]("TITLE")
  def publisherId = column[Int]("PUBLISHER_ID")

  override def * = (id.?, title, publisherId.?) <> (Book.tupled, Book.unapply)

  def publisher = foreignKey("PUBLISHER_FK", publisherId, PublisherEntity.publishers)(_.id)
}

object BookEntity {

  val books: TableQuery[BookEntity] = TableQuery[BookEntity]

  def create(book: Book)(implicit db: Database) = {
    (books returning books.map(_.id) += book).statements.foreach(println)
    db.run(books returning books.map(_.id) += book)
  }

  def findById(id: Int)(implicit db: Database): Future[Option[Book]] = {
    books.filter(_.id === id).result.headOption.statements.foreach(println)
    db.run(books.filter(_.id === id).result.headOption)
  }

  def all()(implicit db: Database): Future[Seq[Book]] = {
    books.sortBy(_.id).result.statements.foreach(println)
    db.run(books.sortBy(_.id).result).map { row =>
      row.map((x) => Book(x.id, x.title, x.publisherId))
    }
  }

  def findTupleById(id: Int)(implicit db: Database): Future[Seq[(Publisher, Book)]] = {

    val q = for {
      b <- BookEntity.books
      p <- b.publisher
      if b.id === id
    } yield (p, b)

    q.result.statements.foreach(println)

    db.run(q.result)
  }
}