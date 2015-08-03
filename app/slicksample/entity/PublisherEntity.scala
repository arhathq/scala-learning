package slicksample.entity

import slick.driver.H2Driver.api._
import slicksample.model.Publisher

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Alexander Kuleshov
 */
class PublisherEntity(tag: Tag) extends Table[Publisher](tag, "PUBLISHER") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  override def * = (id.?, name) <> (Publisher.tupled, Publisher.unapply)
}

object PublisherEntity {

  val publishers = TableQuery[PublisherEntity]

  def create(publisher: Publisher)(implicit db: Database) = {
    (publishers returning publishers.map(_.id) += publisher).statements.foreach(println)
    db.run(publishers returning publishers.map(_.id) += publisher)
  }

  def findById(id: Int)(implicit db: Database): Future[Option[Publisher]] = {
    //    publishers.filter(_.id === id).result.headOption.statements.foreach(println)
    db.run(publishers.filter(_.id === id).result.headOption)
  }

  def all()(implicit db: Database): Future[Seq[Publisher]] = {
    publishers.sortBy(_.name).result.statements.foreach(println)
    db.run(publishers.sortBy(_.name).result).map { row =>
      row.map((x) => Publisher(x.id, x.name))
    }
  }

  def count(implicit db: Database): Future[Int] = {
    //    Query(publishers.length).result.head.statements.foreach(println)
    db.run(Query(publishers.length).result.head)
  }

  def delete(id: Int)(implicit db: Database) = {
    //    publishers.filter(_.id === id).delete.statements.foreach(println)
    db.run(publishers.filter(_.id === id).delete)
  }

  def update(publisher: Publisher)(implicit db: Database) = {
    val copied = publisher.copy()
    //    publishers.filter(_.id === copied.id).update(copied).statements.foreach(println)
    db.run(publishers.filter(_.id === copied.id).update(copied))
  }

}


