#include <QColor>
#include <QApplication>

#include <boost/optional.hpp>

#include <cg/visualization/viewer_adapter.h>
#include <cg/visualization/draw_util.h>

#include <cg/io/point.h>

#include <cg/primitives/point.h>
#include <cg/primitives/segment.h>
#include <cg/operations/diametr_of_set.h>
#include <cmath>

using cg::point_2f;
using cg::point_2;

struct diametr_set_viewer : cg::visualization::viewer_adapter
{
    diametr_set_viewer() : found(false)
   {}

   void draw(cg::visualization::drawer_type & drawer) const
   {
      drawer.set_color(Qt::green);
      for (point_2 const & p : points)
        drawer.draw_point(p, 3);
      if (found) {
        drawer.set_color(Qt::yellow);
        drawer.draw_line(*(result.first), *(result.second));
      }
   }

   void print(cg::visualization::printer_type & p) const
   {
      p.corner_stream() << "press mouse rbutton to add point" << cg::visualization::endl;
   }

   void build() {
       result = cg::diametr_set(points.begin(), points.end());
       found = true;
   }

   bool on_release(const point_2f & p)
   {
      points.push_back(p);
      build();
      return true;
   }

   bool on_key(int key)
   {
      switch (key)
      {
      default : return false;
      }

      build();
      return true;
   }

private:
   std::vector<cg::point_2> points;
   bool found;
   std::pair<std::vector<cg::point_2>::iterator, std::vector<cg::point_2>::iterator> result;
};

int main(int argc, char ** argv)
{
   QApplication app(argc, argv);
   diametr_set_viewer viewer;
   cg::visualization::run_viewer(&viewer, "Diametr of set");
}
