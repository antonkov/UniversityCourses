import java.io._
import java.util._

/**
 * Created by antonkov on 9/17/15.
 */
object A {
  def solve(in: Scanner, out: PrintWriter): Unit = {
    val n = in.nextInt()
    val a = new Array[Int](n)
    val was = new Array[Boolean](n)
    val good = new Array[Boolean](n)
    for (i <- 0 until n) {
      a(i) = in.nextInt() - 1
      was(a(i)) = true
      if (a(i) != 0 && was(a(i) - 1)) {
        good(a(i)) = true
      }
    }
    var cur, best = 1
    for (i <- 0 until n) if (good(i)) {
      cur += 1
      if (cur > best) best = cur
    } else {
      cur = 1
    }
    out.println(n - best)
  }

  def main (args: Array[String]): Unit = {
    val in = new Scanner()
    val out = new PrintWriter(System.out)

    solve(in, out)

    out.close()
  }

  class Scanner {
    val br = new BufferedReader(new InputStreamReader(System.in))
    var st = new StringTokenizer(br.readLine())

    def next(): String = if (st.hasMoreTokens) st.nextToken() else {
      st = new StringTokenizer(br.readLine())
      next()
    }

    def nextInt() = next().toInt
    def nextLong() = next().toLong
    def nextDouble() = next().toDouble
  }
}
