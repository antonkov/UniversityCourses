#include <QColor>
#include <QApplication>

#include <boost/optional.hpp>
#include <boost/utility.hpp>

#include "cg/visualization/viewer_adapter.h"
#include "cg/visualization/draw_util.h"

#include "cg/io/point.h"

#include <cg/primitives/triangle.h>
#include <cg/primitives/point.h>

#include <cg/operations/contains/contour_point.h>
#include <cg/operations/douglas-peucker.h>
#include <vector>

using cg::point_2f;
using cg::point_2;

struct douglas_peucker_viewer : cg::visualization::viewer_adapter
{
    douglas_peucker_viewer() : eps(0.01), simplified(false)
   {}

   void draw(cg::visualization::drawer_type & drawer) const override
   {
      drawer.set_color(Qt::white);

      std::vector<cg::point_2> const & draw_l(simplified ? simplified_line : line);
      if (draw_l.size() < 2) return;
      for (size_t l = 0; l != draw_l.size() - 1; l++)
         drawer.draw_line(draw_l[l], draw_l[l + 1]);
   }

   void print(cg::visualization::printer_type & p) const override
   {
      p.corner_stream() << "eps : " << eps
                        << cg::visualization::endl;
   }

   void update()
   {
       if (line.size() < 2) return;
       simplified_line.clear();
       cg::simplify(line.begin(), line.end(), std::back_inserter(simplified_line), eps);
   }

   bool on_key(int key)
   {
      switch (key)
      {
      case Qt::Key_Plus :
         eps += 1;
         break;

      case Qt::Key_Minus :
         eps -= 1;
         break;

      case Qt::Key_Q :
          simplified ^= 1;
          break;

      default :
         return false;
      }

      update();
      return true;
   }

   bool on_press(const point_2f & p) override
   {

      return false;
   }

   bool on_release(const point_2f & p) override
   {
      line.push_back(p);
      update();
      return false;
   }

   bool on_move(const point_2f & p) override
   {
      return true;
   }

private:
   std::vector<cg::point_2> line, simplified_line;
   bool simplified;
   double eps;
};

int main(int argc, char ** argv)
{
   QApplication app(argc, argv);
   douglas_peucker_viewer viewer;
   cg::visualization::run_viewer(&viewer, "Douglas-Peucker point");
}

