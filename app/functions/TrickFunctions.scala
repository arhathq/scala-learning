package functions

/**
 * @author Alexander Kuleshov
 */
object TrickFunctions {

  def printAll(s1: String, s2: String, s3: String): Unit = {
    println(s1 + ", " + s2 + ", " + s3)
  }

  def printIn(func: (String, String, String) => Unit, s: String): (String, String) => Unit = {
    def innerPrint(s1: String, s2: String): Unit = {
      func(s, s1, s2)
    }
    innerPrint
  }

  def printIn2(func: (String, String, String) => Unit, s: String): (String, String) => Unit = {
    new ((String, String) => Unit) {
      def apply(s1: String, s2: String): Unit = {
        func(s, s1, s2)
      }
    }
  }

  def main(args: Array[String]) {

    val printer = printIn(printAll, "a")
    val printer2 = printIn2(printAll, "b")

    printer("ss", "q")
    printer2("ss", "q")
  }
}
