import java.io.{File, PrintWriter}
import java.util
import java.util.Scanner

import scala.collection.immutable.TreeMap
import scala.collection.mutable.ArrayBuffer

/**
  * Created by antonkov on 12/19/15.
  */
object ConvolutionalCodes {

  type Koefs = Array[Array[Int]]

  case class Edge(state: State, out: List[Int])

  case class State(num: List[Int]) {
    val to = new Array[Edge](2)

    def name(): String = "g_" + num.mkString + "(D,I)"
  }

  def printStates(states: Iterable[State]): Unit = {
    for (state <- states) {
      println(state.num)
      for (i <- state.to.indices) {
        val e = state.to(i)
        println("by: " + i + ", out: " + e.out + " " + ", to:" + e.state.num)
      }
    }
  }

  def printKoefs(koefs: Koefs, maxLines: Int = 10): Unit = {
    var cnt = 0
    val was = new Array[Boolean](100)
    for (d <- koefs.indices) {
      for (i <- koefs(d).indices) {
        val k = koefs(d)(i)
        if (k != 0) {
          if (!was(d)) {
            cnt += 1
            was(d) = true
          }
          if (cnt > maxLines) {
            return
          }
          println(k + " " + "D^" + d + " " + "I^" + i)
        }
      }
    }
  }

  def printDerivs(ds: Array[Int], maxLines: Int = 10): Unit = {
    var cnt = 0
    for (d <- ds.indices) {
      if (ds(d) != 0) {
        cnt += 1
        if (cnt > maxLines) {
          return
        }
        println(ds(d) + " D^" + d)
      }
    }
  }

  case class IncomeEdge(from: Int, D: Int, I: Int)

  type Graph[T] = Array[ArrayBuffer[T]]

  def toId(s: List[Int]): Int = (0 /: s) { (z, x) => z * 2 + x }

  def buildSystem(states: Array[State]): Graph[IncomeEdge] = {
    val n = states.length
    val g = Array.fill(n) {
      new ArrayBuffer[IncomeEdge]()
    }
    for (state <- states) {
      val id = toId(state.num)
      for (i <- state.to.indices) {
        val e = state.to(i)
        val incomeEdge = new IncomeEdge(id, e.out.count(_ != 0), i)
        val newId = toId(e.state.num)
        if (id != 0 || newId != 0) {
          g(newId) += incomeEdge
        }
      }
    }
    return g
  }

  def printSystem(startId: Int, g: Graph[IncomeEdge]): Unit = {
    def printId(id: Int) = print("g_" + id + "(D, I)")
    for (id <- g.indices) {
      printId(id)
      print(" = ")
      var was = false
      for (i <- g(id).indices) {
        if (was) {
          print(" + ")
        }
        val edge = g(id)(i)
        def printPower(p: Int, s: String): Unit = {
          if (p > 1) print(s + "^" + p + " ")
          if (p == 1) print(s + " ")
        }
        printPower(edge.D, "D")
        printPower(edge.I, "I")
        if (edge.from == startId) {
          if (id != startId) {
            print(1)
            was = true
          }
        } else {
          printId(edge.from)
          was = true
        }
      }
      println
    }
  }

  def buildGraph(n: Int, v: Int, gInt: Array[Int], testMaxD: Int): Unit = {
    def toList(x: Int) = {
      var res: List[Int] = List()
      for (i <- 2 to 0 by -1) {
        res = ((x >> i) & 1) :: res
      }
      res
    }
    val g = gInt map toList

    var states = new TreeMap[List[Int], State]()(Ordering.by(_.mkString))
    def go(num: List[Int]): State = {
      if (states contains num) {
        return states(num)
      }
      val state = new State(num)
      states += (num -> state)
      def make(u: Int): Unit = {
        val s = u :: num
        var out: List[Int] = List()
        for (gi <- g) {
          val x = (0 /: (gi zip s)) { (z, u) => {
            val (value, mask) = u
            z ^ (value & mask)
          }
          }
          out = x :: out
        }
        state.to(u) = new Edge(go(s.dropRight(1)), out.reverse)
      }
      make(0)
      make(1)
      return state
    }
    val startStateNum = List.fill(v)(0)
    go(startStateNum)
    //printStates(states.values)
    val functionG = buildSystem(states.values.toArray)
    printSystem(toId(startStateNum), functionG)
    val koefs = solveSystem(testMaxD, testMaxD, functionG)
    println("T(D,I)")
    printKoefs(koefs)
    println("F(D)")
    printDerivs(derivativeI(koefs))

  }

  def derivativeI(di: Koefs): Array[Int] = {
    val res = new Array[Int](di.length)
    for (d <- di.indices) {
      for (i <- di(d).indices) {
        val k = di(d)(i)
        res(d) += i * k
      }
    }
    return res
  }


  def solveSystem(maxD: Int, maxI: Int, g: Graph[IncomeEdge]): Koefs = {
    val res = Array.ofDim[Int](maxD + 1, maxI + 1)
    val q = new util.ArrayDeque[(Int, Int, Int)]()
    q.addLast((0, 0, 0))
    while (!q.isEmpty) {
      val (v, d, i) = q.pollFirst()
      if (d <= maxD && i <= maxI) {
        if (v == 0 && d + i != 0) {
          res(d)(i) += 1
        } else {
          for (e <- g(v)) {
            q.addLast((e.from, d + e.D, i + e.I))
          }
        }
      }
    }
    return res
  }

  def solve(in: Scanner, out: PrintWriter): Unit = {
    val tests = List((30, Array(5, 2, 7)),
      (20, Array(6, 4, 3)),
      (30, Array(5, 5, 7)),
      (30, Array(7, 5, 1)))
    for ((max, test) <- tests) {
      println("Test " + test.mkString(" "))
      buildGraph(3, 2, test, max)
      println
    }
  }

  def main(args: Array[String]) {
    val in = new Scanner(System.in)
    val out = new PrintWriter(System.out)

    solve(in, out)

    out.close()
  }
}
