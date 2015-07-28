package variance

/**
 * @author Alexander Kuleshov
 */
object Variances {
  val invar1: InVar[String] = new InVar[String]("str")
  //  val invar2: InVar[String] = new InVar[AnyRef] // won't compile
  //  val invar3: InVar[AnyRef] = new InVar[String] // won't compile

  val covar1: CoVar[String] = new CoVar[String]("str")
  //  val covar2: CoVar[String] = new CoVar[AnyRef] // won't compile
  val covar3: CoVar[AnyRef] = new CoVar[String]("str")

  val contravar1: ContraVar[String] = new ContraVar[String]("str")
  val contravar2: ContraVar[String] = new ContraVar[AnyRef]("str")
//  val contravar3: ContraVar[AnyRef] = new ContraVar[String] // won't compile
}
