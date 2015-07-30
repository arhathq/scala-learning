package objects

/**
 * @author Alexander Kuleshov
 */
class Employee private (id: Int, firstName: String, lastName: String) {
  private var pwd = ""

  def this(firstName: String, lastName: String) {
    this(0, firstName, lastName)
  }

  def password_=(pwd: String) = {
    if (pwd.length < 5) throw new RuntimeException(Employee.createErrorMessage(2)) else this.pwd = pwd
  }

  def password = pwd

  override def toString() = {
    "Employee(id=%d)"
  }
}

object Employee {
  def apply(id: Int, firstName: String, lastName: String) = new Employee(id, firstName, lastName)

  def createErrorMessage(errorCode: Int): String = errorCode match {
    case 1 => "System Error"
    case 2 => "App Error"
    case _ => "Unknown code!"
  }

  def main(args: Array[String]) = {
    val emp1 = Employee.apply(1, "John", "Doe")
    val emp2 = Employee(2, "Jane", "Doe")

    println(emp1)
    println(emp2)

    emp1.password = "222555"
    println(emp1.password)

    val emp3 = new Employee("Ivan", "Ivanov")
    println(emp3)

    println(Employee.createErrorMessage(1))
    println(Employee.createErrorMessage(2))
    println(Employee.createErrorMessage(3))

  }
}