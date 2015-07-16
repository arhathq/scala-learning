package functions

import scala.annotation.tailrec

/**
 * @author Alexander Kuleshov
 */
object CurriedFunctions {

  def add(x: Int, y: Int): Int = {
    x + y
  }

  val addCurried = (add _).curried

  val addUncurried = Function.uncurried(addCurried)

  def filter[A](condition: A => Boolean)(list: List[A]): List[A] = {
    lazy val recursive = filter(condition) _

    list match {
      case head :: tail =>
        if (condition(head)) head :: recursive(tail)
        else recursive(tail)
      case Nil => Nil
    }
  }

  def filter2[A](condition: A => Boolean)(list: List[A]): List[A] = {
    @tailrec def rec[A](c: A => Boolean)(list: List[A], acc: List[A]) : List[A] = {
      list match {
        case head :: tail =>
          if (c(head)) rec(c)(tail, head :: acc)
          else rec(c)(tail, acc)
        case Nil => acc
      }
    }
    rec(condition)(list, List()).reverse
  }

  def main(args: Array[String]) {
    println("add(1, 2): " + add(1, 2))
    println("addCurried(1)(2): " + addCurried(1)(2))
    println("addUncurried(1, 2): " + addUncurried(1, 2))

    var list = List(1, -2, 3, 4, -5, 0, 6, -7, 8, 9, -10)
    1 to 15 foreach( x => list = list ++ list )

    val positive = (i: Int) => i > 0
    val negative = (i: Int) => i < 0

    println("filter: " + filter(positive)(List(1, -2, 3, 4, -5, 6, -7, 8, 9, -10)))
    println("filter2: " + filter2(negative)(list))

    import DataProcessor._
    import DataStorage._

    val condition = (m: Metadata, d: Data) => m.name == d.name


    val nodes = metaDataList.map ( (m) => process(condition)(m, dataList) )
    for (node <- nodes) println(node)
  }
}

object DataProcessor {

  case class Data(name: String, params: Map[String, Any], children: List[Data])
  case class Metadata(name: String, children: List[Metadata])
  case class DataNode(metadata: Metadata, data: Data)

  def process(condition: (Metadata, Data) => Boolean)(metadata: Metadata, dataList: List[Data]): List[DataNode] = {
    @tailrec def processAcc(c: (Metadata, Data) => Boolean)(m: Metadata, dl: List[Data], acc: List[DataNode]): List[DataNode] = {
      dl match {
        case head :: tail => if (condition(metadata, head)) {
          val node = new DataNode(metadata, head)
          processAcc(condition)(metadata, tail, node :: acc)
        } else processAcc(condition)(metadata, tail, acc)
        case Nil => acc
      }
    }
    processAcc(condition)(metadata, dataList, List())
  }
}

object DataStorage {

  import DataProcessor._

  val metaDataList = List(
    new Metadata("user", List()),
    new Metadata("services", List()),
    new Metadata("operations", List())
  )

  val dataList = List(
    new Data(
      "user",
      Map(
        "username" -> "John001",
        "firstName" -> "John",
        "lastName" -> "Doe"
      ),
      List()),
    new Data(
      "user",
      Map(
        "username" -> "Viktor228",
        "firstName" -> "Viktor",
        "lastName" -> "Petrov"
      ),
      List()),
    new Data(
      "credentials",
      Map(
        "path" -> "/"
      ),
      List()),
    new Data(
      "services",
      Map(),
      List(
        new Data(
          "service",
          Map(
            "type" -> "internet",
            "vaue" -> "Record5"
          ),
          List()
        ),
        new Data(
          "service",
          Map(
            "type" -> "tv",
            "vaue" -> "Zafira"
          ),
          List()
        )
      ))
  )

}
