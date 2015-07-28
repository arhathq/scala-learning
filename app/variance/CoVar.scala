package variance

/**
 * @author Alexander Kuleshov
 */
class CoVar[+T](param1: T) {
  override def toString = "CoVar"

//  def method1(param2: T) = { }   // Error: Covariant type T occurs in contravariant position in type T of value param2
  def method2: T = { param1 }
  def method3: List[T] = { List[T](param1) }
  def method4[U >: T]: List[U] = { List[U](param1) }
  val val1: T = method2
  val val2: Any = param1
//  var var1: T = method2          // Error: Covariant type T occurs in contravariant position in type T of value var1
  var var2: Any = param1
}
