package slicksample.entity

import slick.driver.H2Driver.api._
import slicksample.model.Author

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Alexander Kuleshov
 */
class AuthorEntity(tag: Tag) extends Table[Author](tag, "AUTHOR") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("FIRST_NAME")
  def lastName = column[String]("LAST_NAME")
  // Every table needs a * projection with the same type as the table's type parameter
  override def * = (id.?, firstName, lastName) <> (Author.tupled, Author.unapply)
}

object AuthorEntity {

  val authors: TableQuery[AuthorEntity] = TableQuery[AuthorEntity]

  def create(author: Author)(implicit db: Database) = {
    db.run(authors returning authors.map(_.id) += author)
  }

  def findById(id: Int)(implicit db: Database): Future[Option[Author]] = {
    db.run(authors.filter(_.id === id).result.headOption)
  }

  def all()(implicit db: Database): Future[Seq[Author]] = {
//    db.run(authors.sortBy(_.id).result.map(println))
    db.run(authors.sortBy(_.lastName).result).map { row =>
      row.map((x) => Author(x.id, x.firstName, x.lastName))
    }
  }

  def count(implicit db: Database): Future[Int] = {
    db.run(Query(authors.length).result.head)
  }

  def delete(id: Int)(implicit db: Database) = {
    db.run(authors.filter(_.id === id).delete)
  }

  def update(author: Author)(implicit db: Database) = {
    val copied = author.copy()
    db.run(authors.filter(_.id === copied.id).update(copied))
  }

}