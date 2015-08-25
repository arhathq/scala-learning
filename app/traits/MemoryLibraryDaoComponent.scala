package traits

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

import slicksample.model.{Publisher, Author, Book, Entity}

/**
 * @author Alexander Kuleshov
 */
trait MemoryLibraryDaoComponent extends LibraryDaoComponent {

  override val getLibraryDao: MemoryLibraryDao = new MemoryLibraryDao()

  object Locker {
    def locked[A](f: => A)(implicit lock: ReentrantLock): A = {
      lock.lock()
      val result = f
      lock.unlock()
      result
    }
  }

  import Locker._

  class Storage[A <: Entity[A]] {
    private implicit val lock: ReentrantLock = new ReentrantLock()
    private val idGenerator: AtomicInteger = new AtomicInteger()
    private var data: List[A] = List.empty[A]

    def find(id: Int): Option[A] = {
      data.find(_.getId().get == id)
    }

    def insert(entity: A): Int = locked {
      val persisted: A = entity.newEntity(idGenerator.incrementAndGet())
      data = persisted :: data
      persisted.getId().get
    }

    def update(entity: A) = locked {
      data = entity :: data.filterNot(_.getId().get == entity.getId().get)
    }

    def delete(id: Int) = locked {
      data = data.filterNot(_.getId().contains(id))
    }
  }

  class MemoryLibraryDao extends LibraryDao {
    val authorStorage: Storage[Author] = new Storage[Author]
    val publisherStorage: Storage[Publisher] = new Storage[Publisher]
    val bookStorage: Storage[Book] = new Storage[Book]
  }
}

