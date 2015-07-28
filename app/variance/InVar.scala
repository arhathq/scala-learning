package variance

/**
 * @author Alexander Kuleshov
 */
class InVar[T](param1: T) {
  override def toString = "InVar"

  def method1(param2: T) = { }
  def method2: T = { param1 }
  def method3: List[T] = { List[T](param1) }
  def method4[U >: T]: List[U] = { List[U](param1) }
  val val1: T = method2
  val val2: Any = param1
  var var1: T = method2
  var var2: Any = param1
}