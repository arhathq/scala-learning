package folding

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
    case Nil => 0.0
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
  }
}