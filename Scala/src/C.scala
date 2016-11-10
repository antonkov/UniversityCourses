import java.io._
import java.util._

import scala.collection.mutable

/**
  * Created by antonkov on 9/17/15.
  */
object C {

  class Point(val x: Int, val y: Int) {

    override def toString = s"Point($x, $y)"
  }

  def convexHull(input: Array[Point]): Array[Point] = {
    val pts = input.sortBy(_.x)
    val convex = new mutable.ArrayStack[Point]()
    convex.push(new Point(0, pts.maxBy(_.y).y))
    def bad(p0: Point, p1: Point, p2: Point): Boolean = {
      val x1 = p1.x - p0.x
      val x2 = p2.x - p0.x
      val y1 = p1.y - p0.y
      val y2 = p2.y - p0.y
      x1 * y2 - x2 * y1 >= 0
    }
    for (pt <- pts) {
      while (convex.size >= 2 && bad(convex(1), convex(0), pt)) {
        convex.pop()
      }
      convex.push(pt)
    }
    convex.reverse.toArray
  }

  def solve(in: Scanner, out: PrintWriter): Unit = {
    val eps = 1e-9
    val n, p, q = in.nextInt()
    var pts = new Array[Point](n)
    for (i <- 0 until n) {
      pts(i) = new Point(in.nextInt(), in.nextInt())
    }
    pts = convexHull(pts)
    var (l, r): (Double, Double) = (0.0, Math.max(p, q))
    for (iter <- 1 to 100) {
      val mid = (l + r) / 2
      val willX = p / mid
      val shallY = q / mid
      if (pts.last.x + eps < willX) {
        l = mid
      } else {
        var (pl, pr) = (0, pts.size)
        while (pr - pl > 1) {
          val pm = (pl + pr) / 2
          if (pts(pm).x + eps < willX) {
            pl = pm
          } else {
            pr = pm
          }
        }
        val dx: Double = pts(pl + 1).x - pts(pl).x
        val dy: Double = pts(pl + 1).y - pts(pl).y
        val willY = pts(pl).y + dy / dx * (willX - pts(pl).x)
        if (willY + eps > shallY) {
          r = mid
        } else {
          l = mid
        }
      }

    }
    out.println(l)
  }

  def main(args: Array[String]): Unit = {
    val in = new Scanner()
    val out = new PrintWriter(System.out)

    solve(in, out)

    out.close()
  }

  class Scanner {
    val br = new BufferedReader(new InputStreamReader(System.in))
    var st = new StringTokenizer(br.readLine())

    def next(): String = if (st.hasMoreTokens) st.nextToken()
    else {
      st = new StringTokenizer(br.readLine())
      next()
    }

    def nextInt() = next().toInt

    def nextLong() = next().toLong

    def nextDouble() = next().toDouble
  }

}
