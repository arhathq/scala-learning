package functions

/**
 * @author Alexander Kuleshov
 */
object FoldFuctions {

  def sum(list: List[Int]): Int = list.foldLeft(0)((sum, el) => { sum + el})

  def sum2(list: List[Int]): Int = list.foldLeft(0)(_ + _)

  def product(list: List[Int]): Int = list.foldLeft(1)(_ * _)

  def count(list: List[Any]): Int = list.foldLeft(0)((count, _) => count + 1)

  def average(list: List[Double]): Double = {
    list.foldLeft(0.0)(_ + _) / list.foldLeft(0)((count, _) => count + 1)
  }

  def average2(list: List[Double]): Double = list match {
    case head :: tail => tail.foldLeft( (head, 1.0) )((acc, el) => {
      ((acc._1 + (el / acc._2)) * acc._2 / (acc._2 + 1), acc._2 + 1)
    })._1
    case Nil => Double.NaN
  }

  def last[A](list: List[A]): A = list.foldLeft[A](list.head)((_, el) => el)

  def penultimate[A](list: List[A]): A =
    list.foldLeft( (list.head, list.tail.head) )((acc, el) => (acc._2, el))._1

  def contains[A](list: List[A], e: A): Boolean = list.foldLeft(false)(_ || _ == e)

  def get[A](list: List[A], index: Int): A = list.foldLeft( (0, list.head) )((acc, el) => {
    if (acc._1 == index) acc else (acc._1 + 1, el)
  }) match {
    case (idx, res) if idx == index => res
    case _ => throw new Exception("Bad index")
  }

  def toString[A](list: List[A]): String = list match {
    case head :: tail => tail.foldLeft("List(" + head)(_ + ", " + _) + ")"
    case Nil => "List()"
  }

  def reverse[A](list: List[A]): List[A] = {
    list.foldLeft(List[A]())((acc, el) => el :: acc)
  }

  def unique[A](list: List[A]): List[A] = {
    list.foldLeft(List[A]())((acc, el) => if (acc.contains(el)) acc else el :: acc).reverse
  }

  def unique2[A](list: List[A]): List[A] = {
    list.foldLeft(List[A]())((acc, el) => if (acc.contains(el)) acc else el :: acc).
      foldLeft(List[A]())((acc, el) => el :: acc)
  }

  def toSet[A](list: List[A]): Set[A] = {
    list.foldLeft(Set[A]())((acc, el) => acc + el)
  }

  def doubleElements[A](list: List[A]): List[A] = {
    list.foldLeft(List[A]())((acc, el) => el :: el :: acc).reverse
  }

  def doubleElements2[A](list: List[A]): List[A] = {
    list.foldRight(List[A]())((el, acc) => el :: el :: acc)
  }

  def insertionSort[A](list: List[A])(implicit ev1: A => Ordered[A]): List[A] = {
    list.foldLeft(List[A]()) { (acc, el) =>
      val (left, right) = acc.span(_ < el)
      left ::: el :: right
    }
  }

  def pivot[A](list: List[A])(implicit ev1: A => Ordered[A]): (List[A], A, List[A]) = {
    list.foldLeft[(List[A], A, List[A])]((Nil, list.head, Nil)) { (acc, el) =>
      val (left, pivot, right) = acc
      if (el <= pivot) (el :: left, pivot, right) else (left, pivot, el :: right)
    }
  }

  def quickSort[A](list: List[A])(implicit ev1: A => Ordered[A]): List[A] = list match {
    case head :: _ :: _ =>
      list.foldLeft[(List[A], List[A], List[A])]((Nil, Nil, Nil)) { (acc, el) =>
        val (left, center, right) = acc
        if (el < head) (el :: left, center, right)
        else if (el > head) (left, center, el :: right)
        else (left, el :: center, right)
      } match {
        case (list1, list2, list3) => quickSort(list1) ::: list2 ::: quickSort(list3)
      }
    case _ => list
  }

  def encode[A](list: List[A]): List[(A, Int)] = {
    list.foldLeft(List[(A, Int)]()) { (acc, el) =>
      if (acc.isEmpty) (el, 1) :: acc
      else {
        if (acc.tail.isEmpty) (el, 1) :: acc
        else {
          val (node, number) = acc.head
          if (el == node) (node, number + 1) :: acc.tail
          else (el, 1) :: acc
        }
      }
    }.reverse
  }

  def encode2[A](list: List[A]): List[(A, Int)] = {
    list.foldLeft(List[(A, Int)]()) { (acc, el) =>
      acc match {
        case (node, number) :: tail =>
          if (el == node) (node, number + 1) :: acc.tail
          else (el, 1) :: acc
        case Nil => (el, 1) :: acc
      }
    }.reverse
  }

  def decode[A](list: List[(A, Int)]): List[A] = {
    list.foldLeft[List[A]](List()) { (acc, el) =>
      var res = acc
      val (node, number) = el
      for (_ <- 1 to number) res = node :: res
      res
    }.reverse
  }

  def group[A](list: List[A], size: Int): List[List[A]] = {
    list.foldLeft( (List[List[A]](), 0) ) { (acc, el) =>
      acc match {
        case (head :: tail, count) =>
          if (count < size) ((el :: head) :: tail, count + 1)
          else (List(el) :: head :: tail , 1)
        case (Nil, count) => (List(List(el)), 1)
      }
    }._1.foldLeft(List[List[A]]()) { (acc, el) =>
      el.reverse :: acc

    }
  }

  def main(args: Array[String]) {
    val list = List(1, 2, 3, 4, 5)

    println("sum: " + sum(list))
    println("sum2: " + sum2(list))
    println("product: " + product(list))
    println("count: " + count(list))

    println("average: " + average(List(1.0, 2.0, 3.0)))
    println("average2: " + average2(List(1.0, 2.0, 3.0)))

    println("last: " + last(list))
    println("penultimate: " + penultimate(list))

    println("contains: " + contains(list, 6))
    println("contains: " + contains(list, 3))

    println("get: " + get(list.reverse, 2))

    println("toString: " + toString(list))
    println("toString: " + toString(List()))
    println("toString: " + toString(Nil))

    println("reverse: " + reverse(list))

    println("unique: " + unique(List(1, 2, 3, 4, 2, 1, 6, 5, 1)))
    println("unique2: " + unique2(List(1, 2, 3, 4, 2, 1, 6, 5, 1)))
    println("toSet: " + toSet(List(1, 2, 3, 4, 2, 1, 6, 5, 1)))

    println("doubleElements: " + doubleElements(list))
    println("doubleElements2: " + doubleElements2(list))

    println("insertionSort: " + insertionSort(List(4, 5, 1, 3, 2, 6)))
    println("pivot: " + pivot(List(4, 5, 1, 3, 2, 6)))
    println("quickSort: " + quickSort(List(4, 5, 1, 3, 2, 6)))

    println("encode: " + encode(List(4, 5, 5, 1, 3, 2, 2, 2, 6)))
    println("encode2: " + encode2(List(4, 5, 5, 1, 3, 2, 2, 2, 6)))
    println("decode: " + decode(List((4,1), (5,2), (1,1), (3,1), (2,3), (6,1))))

    println("group: " + group(List(4, 5, 5, 1, 3, 2, 2, 2, 6), 2))
  }
}