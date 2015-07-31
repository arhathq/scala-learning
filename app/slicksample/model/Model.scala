package slicksample.model

/**
 * @author Alexander Kuleshov
 */
case class Author(id: Option[Int] = None, firstName: String, lastName: String)
case class Book(id: Option[Int] = None, title: String, authors: List[Author] = List())
