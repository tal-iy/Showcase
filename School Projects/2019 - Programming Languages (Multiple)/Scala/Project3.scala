
/*
 * Assignment: Project 3
 * Author: Vitaliy Shydlonok
 * Date: 10/23/2019
*/

//  S  -: E$
//  E  -: T E2
//  E2 -: '|' E3
//  E2 -: NIL
//  E3 -: T E2
//  T  -: F T2
//  T2 -: F T2
//  T2 -: NIL
//  F  -: A F2
//  F2 -: '?' F2
//  F2 -: NIL
//  A  -: C
//  A  -: '(' A2
//  A2 -: E ')'

class Environment(var input: String, var index: Int = 0) { }

abstract class S { // Expression
  def eval(env: Environment): Boolean
}

case class E(l: T, r: Option[E2]) extends S { // | statement
  def eval(env: Environment): Boolean = {
    r match {
      case Some(r) => {
        val index = env.index
        // Try to evaluate the left side
        if (l.eval(env))
          true
        else {
          // Reset the index to evaluate the right side
          env.index = index
          r.eval(env)
        }
      }
      case None => l.eval(env)
    }
  }
}

case class E2(l: E3) extends S { // Right side of | statement
  def eval(env: Environment): Boolean = {
    l.eval(env)
  }
}

case class E3(l: T, r: Option[E2]) extends S { // | statement
  def eval(env: Environment): Boolean = {
    r match {
      case Some(r) =>
        val index = env.index
        // Try to evaluate the left side
        if (l.eval(env))
          true
        else {
          // Reset the index to evaluate the right side
          env.index = index
          r.eval(env)
        }
      case None => l.eval(env)
    }
  }
}

case class T(l: F, r: Option[T2]) extends S { // Concatenation and left side of | statement
  def eval(env: Environment): Boolean = {
    // Determine whether the F has a ?
    var question: Boolean = false
    l.r match {
      case Some(lr) => question = true
      case None => question = false
    }

    r match {
      case Some(r) =>
        if (question) {
          val index = env.index
          if (!r.eval(env)) { // Try the right side first
            env.index = index
            l.eval(env) && r.eval(env) // Try both the left and right side
          } else true
        }
        else l.eval(env) && r.eval(env)
      case None => l.eval(env)
    }
  }
}

case class T2(l: F, r: Option[T2]) extends S { // Right side of concatenation
  def eval(env: Environment): Boolean = {
    r match {
      case Some(r) => l.eval(env) && r.eval(env)
      case None => l.eval(env)
    }
  }
}

case class F(l: A, r: Option[F2]) extends S { // ? statement and left side of concatenation
  def eval(env: Environment): Boolean = {
    // Check whether a ? exists
    r match {
      case Some(r) =>
        val index = env.index
        // Try to evaluate the left side, reset the index if it fails
        if (!l.eval(env))
          env.index = index
        true
      case None => l.eval(env)
    }
  }
}

case class F2(l: Option[F2]) extends S { // Right side of ? statement (can only be another ? char)
  def eval(env: Environment): Boolean = true
}

abstract class A extends S { } // Character or expression and left side of ? statement

case class A2(l: E) extends A {
  def eval(env: Environment): Boolean = {
    l.eval(env)
  }
}

case class C(c: Char) extends A { // Character
  def eval(env: Environment): Boolean = {
    if (env.index < env.input.length && (c == '.' || env.input(env.index) == c)) {
      env.index+=1
      true
    }
    else false
  }
}

class RecursiveDescent(input:String) {

  var index = 0

  def parseS(): S = parseE()

  def parseE(): E = E(parseT(), parseE2())

  def parseE2(): Option[E2] = {
    // Check for the ending of an E
    if (index < input.length && input(index) == ')') {
      index+=1 // Advance past )
      None
    }
    else if (index < input.length && input(index) == '|') {
      index+=1; // Advance past |
      Some(E2(parseE3()))
    }
    else None
  }

  def parseE3(): E3 = E3(parseT(), parseE2())

  def parseT(): T = T(parseF(), parseT2())

  def parseT2(): Option[T2] = {
    // Make sure that the next char is only concatenation
    if (index < input.length && input(index) != '?' && input(index) != '|' && input(index) != ')') {
      Some(T2(parseF(), parseT2()))
    }
    else None
  }

  def parseF(): F = F(parseA(), parseF2())

  def parseF2(): Option[F2] = {
    if (index < input.length && input(index) == '?') {
      index+=1 // Advance past ?
      Some(F2(parseF2()))
    }
    else None
  }

  def parseA(): A = {
    // Check whether the next char is a (
    if (index < input.length && input(index) == '(') {
      index+=1 // Advance past the (
      A2(parseE())
    }
    else {
      index += 1 // Advance past the char
      C(input(index-1))
    }
  }
}

object Main {

  def main(args: Array[String]) {

    // Get an input pattern and build a tree
    print("pattern? ")
    val pattern: String = scala.io.StdIn.readLine()
    val rd = new RecursiveDescent(pattern)
    val exp2rd: S = rd.parseE()
    println(exp2rd)

    // Do pattern matching on input strings
    while(true) {
      // Get and evaluate an input string
      print("string? ")
      val env = new Environment(scala.io.StdIn.readLine())
      val matching = exp2rd.eval(env)

      // Make sure that the input string matched the pattern and the whole string was used
      if (matching && env.index == env.input.length)
        println("match")
      else
        println("no match")
    }
  }
}
