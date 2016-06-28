package traits

/**
  * @author Alexander Kuleshov
  */
object Linearization extends App {
  val instance = new D
  println(instance.whoAmI())
  println(instance.inheritaceList())

  val cell = new StandardCell(1) with Max with Reducing with Doubling
  println(cell.retrieve)
  cell.save(5)
  println(cell.retrieve)
}

trait A {
  def whoAmI() = "A"

  def inheritaceList() = List("A")
}

class B extends A {
  override def whoAmI() = "B"

  override def inheritaceList() = "B" +: super.inheritaceList()
}

trait C extends A {
  abstract override def whoAmI() = "C"

  override def inheritaceList() = "C" +: super.inheritaceList()
}

trait F extends A {
  abstract override def whoAmI() = "F"

  override def inheritaceList() = "F" +: super.inheritaceList()
}

class D extends B with F with C

class E extends D

trait Cell {
  def save(x: Int)
  def retrieve: Any
}

class StandardCell(protected var state: Int) extends Cell {
  def save(x: Int) = state = x
  def retrieve = state
}

trait Doubling extends Cell {
  abstract override def save(x: Int) = super.save(2 * x)
}

trait Max extends Cell {
  abstract override def save(x : Int) = if(x > 10) throw new Exception("overflow!") else super.save(x)
}

trait Reducing extends Cell {
  abstract override def save(x : Int) = if(x < 0) throw new Exception("less than 0!") else super.save(x - 1)
}
