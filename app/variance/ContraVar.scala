package variance

/**
 * @author Alexander Kuleshov
 */
class ContraVar[-T](param1: T) {
  override def toString = "ContraVar"

  def method1(param2: T) = { }
//  def method2: T = { param1 }                        // Error: Contravariant type T occurs in covariant position in type T of value method2
//  def method3: List[T] = { List[T](param1) }         // Error: Contravariant type T occurs in covariant position in type List[T] of value method3
//  def method4[U >: T]: List[U] = { List[U](param1) } // Error: Contravariant type T occurs in covariant position in type T of value U
//  val val1: T = method2                              // Error: Contravariant type T occurs in covariant position in type T of value val1
  val val2: Any = param1
//  var var1: T = method2                              // Error: Contravariant type T occurs in covariant position in type T of value var1
  var var2: Any = param1
}
