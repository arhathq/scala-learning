package folding

import scala.annotation.tailrec

/**
 * @author Alexander Kuleshov
 */
object RecursiveFunctions {

  def listLength[A](list: List[A]): Int = {
    if (list == Nil) 0
    else 1 + listLength(list.tail)
  }

  def listLength2[A](list: List[A]): Int = {
    @tailrec def listLength(list: List[A], acc: Int): Int = {
      if (list == Nil) acc
      else listLength(list.tail, acc + 1)
    }
    listLength(list, 0)
  }

  def main(args: Array[String]) {
    println("listLength: " + listLength(List(1, 2, 3, 4 ,5, 6, 7, 8)))

    var list2 = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    1 to 15 foreach( x => list2 = list2 ++ list2 )

    println("listLength2: " + listLength2(list2))

  }
}
