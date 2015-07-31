package slicksample.entity

import slick.driver.H2Driver.api._
import slicksample.model.Book

/**
 * @author Alexander Kuleshov
 */
//class BookEntity(tag: Tag) extends Table[Book](tag, "BOOK") {
//  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
//  def title = column[String]("TITLE")
//  override def * = (id.?, title) <> (Book.tupled, Book.unapply)
//}
//

object BookEntity {
//
//  val books: TableQuery[BookEntity] = TableQuery[BookEntity]
//
}