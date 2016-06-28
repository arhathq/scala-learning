package regex

import scala.util.parsing.combinator._
import scala.util.matching.Regex

/**
 * @author Alexander Kuleshov
 */
object SvnParser extends RegexParsers {

  private val spaceRegex  = new Regex("[ \\n]+")
  private val numberRegex = new Regex("[0-9]+")
  private val wordRegex   = new Regex("[a-zA-Z][a-zA-Z0-9-]*")

  private def space  = regex(spaceRegex)
  private def regexAndSpace(re: Regex) = regex(re) <~ space

  override def skipWhitespace = false

  def number = regexAndSpace(numberRegex)
  def word   = regexAndSpace(wordRegex)
  def string = regex(numberRegex) >> { len => ":" ~> regexAndSpace(new Regex(".{" + len + "}")) }
  def list: Parser[List[Any]] = "(" ~> space ~> ( item + ) <~ ")" <~ space

  def item = number | word | string | list

  def parseItem(str: String) = parse(item, str)

  def main(args: Array[String]) {
    println(item)

    SvnParser.parseItem("( 5:abcde 3:abc  \n   20:three separate words     (  abc def     \n\n\n   123 ) ) ") match {
      case SvnParser.Success(result, _) => println(result.toString)
      case _ => println("Could not parse the input string.")
    }
  }
}
