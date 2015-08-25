package traits

import slicksample.model.Author

/**
 * @author Alexander Kuleshov
 */
object StorageApp extends App with MemoryLibraryDaoComponent {
  val libraryDao = getLibraryDao

  val id = libraryDao.authorStorage.insert(Author(firstName = "Mark", lastName = "Twain"))
  println(id)

  val author = libraryDao.authorStorage.find(id)
  println(author)
}


