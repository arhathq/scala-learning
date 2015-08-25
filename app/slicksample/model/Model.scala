package slicksample.model

/**
 * @author Alexander Kuleshov
 */
trait Entity[+A] {
  def newEntity(id: Int): A
  def getId(): Option[Int]
}

case class Author(id: Option[Int] = None, firstName: String, lastName: String) extends Entity[Author] {
  override def newEntity(id: Int): Author = copy(id = Some(id))
  override def getId(): Option[Int] = id
}

case class Publisher(id: Option[Int] = None, name: String) extends Entity[Publisher] {
  override def newEntity(id: Int): Publisher = copy(id = Some(id))
  override def getId(): Option[Int] = id
}

case class Book(id: Option[Int] = None, title: String, publisherId: Option[Int] = None) extends Entity[Book] {
  override def newEntity(id: Int): Book = copy(id = Some(id))
  override def getId(): Option[Int] = id
}
