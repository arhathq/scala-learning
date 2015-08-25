package traits

import slicksample.model.{Publisher, Author, Book}

import scala.concurrent.Future

/**
 * @author Alexander Kuleshov
 */
trait LibraryServiceComponent { self: LibraryDaoComponent =>

  def getLibraryService: LibraryService

}

trait LibraryService {

  def deleteBook(id: Long)
  def deleteAuthor(id: Long)
  def deletePublisher(id: Long)

  def saveAuthor(author: Author): Future[Long]
  def saveBook(book: Book): Future[Long]
  def savePublisher(publisher: Publisher): Future[Long]

  def update(id: Long, note: Author)
  def update(id: Long, note: Book)

  def findAllBooks(): Future[List[Book]]
  def findBooksByAuthor(author: Option[String]): Future[List[Book]]
  def findBookById(id:Long): Future[Option[Book]]

  def findAllAuthors(): Future[List[Author]]
  def findAuthorById(id:Long): Future[Option[Author]]

}