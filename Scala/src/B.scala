import java.io._
import java.util._
import scala.collection.JavaConversions._

/**
 * Created by antonkov on 9/17/15.
 */
object B {
  class Edge(val w: Int, val inMst: Boolean, val num: Int) {
    override def toString = s"Edge($w, $inMst, $num)"
  }

  def solve(in: Scanner, out: PrintWriter): Unit = {
    val n, m = in.nextInt()
    var edges = new Array[Edge](m)
    for (i <- 0 until m) {
      edges(i) = new Edge(in.nextInt(), in.nextInt() == 1, i)
    }
    edges = edges.sortWith((e1, e2) => if (e1.w != e2.w) e1.w < e2.w else e2.inMst < e1.inMst )
    val q = new ArrayDeque[(Int, Int)]()
    var v = 1
    val res = new Array[(Int, Int)](m)
    for (e <- edges) {
      if (e.inMst) {
        v += 1
        def addEdges {
          for (u <- 1 until v) {
            if (q.size() >= m)
              return
            q.add((u, v))
          }
        }
        addEdges
        res(e.num) = q.pollLast()
      } else {
        if (q.isEmpty) {
          out.println(-1)
          return
        }
        res(e.num) = q.pollFirst()
      }
    }
    for ((u, v) <- res) {
      out.println(u + " " + v)
    }
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
